package com.caoyong.core.controller.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.product.Brand;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.core.service.SearchService;
import com.caoyong.core.service.product.BrandService;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 前台商品
 * @author yong.cao
 * @time 2017年7月2日下午3:33:18
 */
@Slf4j
@Controller
public class ProductController {
	@Autowired
	private SearchService searchService;
	@Autowired
	private BrandService brandService;
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping(value=("/"))
	public String index(){
		return "index";
	}
	/**
	 * 搜索
	 * @return
	 */
	@RequestMapping(value=("/search"))
	public String search(ProductQueryDTO query, Model model){
		log.info("search start. keyword:{}", ToStringBuilder.reflectionToString
				(query, ToStringStyle.DEFAULT_STYLE));
		try {
			//从redis中查询品牌
			List<Brand> brands = brandService.selectBrandListFromRedis();
			model.addAttribute("brands", brands);
			model.addAttribute("brandId", query.getBrandId());
			model.addAttribute("price", query.getPrice());
			Page<Product> page = searchService.selectPageByQuery(query);
			if(null!=page.getRows() && !page.getRows().isEmpty()){
				model.addAttribute("page", page);
			}
			//设置已选条件
			Map<String, String> selectedTermsMap = new HashMap<>();
			//设置品牌
			if(null != query.getBrandId()){
				String brandName = brands.stream().filter
						(brand -> query.getBrandId().equals(brand.getId()))
						.findFirst().get().getName();
				selectedTermsMap.put("品牌", brandName);
			}
			//设置价格
			if(StringUtils.isNotBlank(query.getPrice())){
				if(query.getPrice().contains("-")){
					selectedTermsMap.put("价格", query.getPrice());
				}else{
					selectedTermsMap.put("价格", query.getPrice() + "以上");
				}
			}
			model.addAttribute("selectedTermsMap", selectedTermsMap);
		} catch (BizException e) {
			log.error("search biz error:{}", e.getMessage(), e );
		} catch (Exception e) {
			log.error("search error:{}", e.getMessage(), e);
		}
		log.info("search end.");
		return "search";
	}
}
