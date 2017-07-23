package com.caoyong.core.controller.cart;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.caoyong.common.utlis.JSONConversionUtil;
import com.caoyong.common.utlis.RequestUtil;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.cart.AddCartDTO;
import com.caoyong.core.bean.cart.BuyerCart;
import com.caoyong.core.bean.cart.BuyerItem;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.service.product.SkuService;
import com.caoyong.core.service.user.SessionProvider;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 购物车controller
 * @author yong.cao
 * @time 2017年7月22日上午6:59:57
 */
@Slf4j
@Controller
public class CartController {
	@Autowired
	private SkuService skuService;
	@Autowired
	private SessionProvider sessionProvider;
	/**
	 * 添加购物车
	 * @param addCartDTO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value=("/addCart"))
	public String addCart(AddCartDTO addCartDTO, Model model, 
			HttpServletRequest request, HttpServletResponse response){
		log.info("addCart start. addCartDTO:{}", ToStringBuilder.
				reflectionToString(addCartDTO, ToStringStyle.DEFAULT_STYLE));
		try {
			//获取购物车
			BuyerCart buyerCart = getBuyerCartFromCookie(request);
			//追加商品到购物车
			Sku sku = new Sku();
			sku.setId(addCartDTO.getSkuId());
			//购物项
			BuyerItem buyerItem = new BuyerItem();
			buyerItem.setSku(sku);
			buyerItem.setAmount(addCartDTO.getAmount());
			//追加
			buyerCart.addItem(buyerItem);
			String username = sessionProvider.getAttributeForUser
					(RequestUtil.getCSESSIONID(request, response));
			//非空表示已登录
			if(StringUtils.isNotEmpty(username)){
				saveBuyerCartToRedis(buyerCart, username, response);
				
			}else{
				//创建购物车cookie
				String cart = JSONConversionUtil.objToString(buyerCart);
				//cookie的value不允许有"和,所以这里要做转换
				cart = cart.replaceAll("\"", "'").replaceAll(",", "#");
				log.info("cart:{}", cart);
				Cookie cookie = new Cookie(Constants.BUYER_CART, cart);
				//cookie有效期7天
				cookie.setMaxAge(60*60*24);
				//设置路径
				cookie.setPath("/");
				//上线后需要设置域名
				response.addCookie(cookie);//回写到浏览器
			}
		} catch (Exception e) {
			log.info("addCartDTO error:{}", e.getMessage(), e);
		}
		return "redirect:/toCart";
	}
	/**
	 * 去购物车
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value=("/toCart"))
	public String toCart(Model model, HttpServletRequest request, 
			HttpServletResponse response){
		log.info("toCart start.");
		try {
			//获取购物车
			BuyerCart buyerCart = getBuyerCartFromCookie(request);
			String username = sessionProvider.getAttributeForUser
					(RequestUtil.getCSESSIONID(request, response));
			//非空表示已登录
			if(StringUtils.isNotEmpty(username)){
				saveBuyerCartToRedis(buyerCart, username, response);
				
				//从redis中查询所有的购物车
				ResultBase<BuyerCart> BuyerCartResult = skuService.
						selectBuyerCartFromRedis(username);
				//redis中的
				buyerCart = BuyerCartResult.getValue();
			}
			List<BuyerItem> items = buyerCart.getItems();
			if(!items.isEmpty()){
				for(BuyerItem buyerItem : items){
					//查询sku详细信息
					buyerItem.setSku(skuService.selectSkuById
							(buyerItem.getSku().getId()).getValue());
				}
			}
			model.addAttribute("buyerCart", buyerCart);
		} catch (BizException e) {
			log.error("toCart biz error:", e.getMessage(), e);
		} catch (Exception e) {
			log.error("toCart error:", e.getMessage(), e);
		}
		log.info("toCart end.");
		return "cart";
	}
	/**
	 * 结算
	 * @param skuIds
	 * @param model
	 * @return
	 */
	@RequestMapping(value=("/buyer/trueBuy"))
	public String trueBuy(Long[] skuIds, Model model, 
			HttpServletRequest request, HttpServletResponse response){
		log.info("trueBuy start, skuIds:{}", ToStringBuilder
				.reflectionToString(skuIds, ToStringStyle.DEFAULT_STYLE));
		try {
			String username = sessionProvider.getAttributeForUser
					(RequestUtil.getCSESSIONID(request, response));
			//从redis中查询所有的购物车
			ResultBase<BuyerCart> BuyerCartResult = skuService.
					selectBuyerCartFromRedis(username);
			//redis中的
			BuyerCart buyerCart = BuyerCartResult.getValue();
			List<BuyerItem> items = buyerCart.getItems();
			if(!items.isEmpty()){
				boolean match = false;
				for(BuyerItem buyerItem : items){
					//查询sku详细信息
					buyerItem.setSku(skuService.selectSkuById
							(buyerItem.getSku().getId()).getValue());
					//设置是否有贷
					if(buyerItem.getAmount() > buyerItem.getSku().getStock()){
						buyerItem.setIsHave(false);
						//当存在无货的时候，把match置为false
						match = true;
					}
				}
				//任意一款无货返回购物车
				if(match){
					model.addAttribute("buyerCart", buyerCart);
					return "cart";
				}
			}else{
				return "redirect:/toCart";
			}
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			log.error("getBuyerCartFromCookie error:{}", e.getMessage(), e);
		}
		log.info("tureBuy end.");
		return "order";
	}
	/**
	 * 从cookie中获取购物车
	 * @param request
	 * @return
	 */
	private BuyerCart getBuyerCartFromCookie(HttpServletRequest request){
		log.info("getBuyerCartFromCookie start");
		BuyerCart buyerCart = null;
		try {
			//判断cookie中有没有购物车
			Cookie[] cookies = request.getCookies();
			if(null !=cookies && cookies.length > 0){
				//遍历之前的cookie
				for (Cookie cookie : cookies) {
					if(Constants.BUYER_CART.equals(cookie.getName())){
						String value = cookie.getValue();
						//cookie的value不允许有"和,所以这里要做转换
						value = value.replaceAll("'", "\"").replaceAll("#", ",");
						log.info("value:{}", value);
						//转成对象
						buyerCart = StringUtils.isNotBlank(value)?
						JSONConversionUtil.stringToObj(value, BuyerCart.class)
						:null;
						break;
					}
				}
				//java8实现
				/*String buyerCartCookie = Stream.of(cookies)
				.filter(cookie -> Constants.BUYER_CART.equals(cookie.getName()))
				.findFirst()
				.map(Cookie :: getValue).get();
				buyerCart = JSONConversionUtil.stringToObj(buyerCartCookie, BuyerCart.class);*/
			}
			//当购物车为空时创建购物车
			buyerCart = null != buyerCart ? buyerCart : new BuyerCart();
		} catch (Exception e) {
			log.error("getBuyerCartFromCookie error:{}", e.getMessage(), e);
		}
		log.info("getBuyerCartFromCookie end.");
		return buyerCart;
	}
	/**
	 * 保存购物车到redis中，并消除cookie
	 * @param buyerCart
	 * @param username
	 * @param response
	 */
	private void saveBuyerCartToRedis(BuyerCart buyerCart, String username, 
			HttpServletResponse response){
		log.info("saveBuyerCartToRedis start. username:{}", username);
		try {
			//保存购物车到redis中
			skuService.insertBuyerCartToRedis(buyerCart, username);
			//消除cookie
			Cookie cookie = new Cookie(Constants.BUYER_CART, null);
			cookie.setMaxAge(-1);
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (BizException e) {
			log.error("saveBuyerCartToRedis biz error:{}", e.getMessage(), e);
		} catch (Exception e) {
			log.error("saveBuyerCartToRedis error:{}", e.getMessage(), e);
		}
		log.info("saveBuyerCartToRedis end.");
	}
}
