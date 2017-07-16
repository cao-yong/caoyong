package com.caoyong.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caoyong.common.utlis.EncodeUtil;
import com.caoyong.common.utlis.RequestUtil;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.login.LoginDTO;
import com.caoyong.core.bean.user.Buyer;
import com.caoyong.core.service.user.BuyerService;
import com.caoyong.core.service.user.SessionProvider;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 单点登录系统
 * @author yong.cao
 * @time 2017年7月15日下午10:35:02
 */
@Slf4j
@Controller
public class LoginController {
	@Autowired
	private BuyerService buyerService;
	@Autowired
	private SessionProvider sessionProvider;
	
	/**
	 * 登录
	 * @return
	 */
	@RequestMapping(value=("/login.aspx"), method=RequestMethod.GET)
	public String login(){
		return "login";
	}
	/**
	 * 判断用户是否登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value=("/isLogin.aspx"))
	public @ResponseBody
	MappingJacksonValue isLogin(HttpServletRequest request, HttpServletResponse response, 
			String callback){
		log.info("isLogin start.");
		MappingJacksonValue mjv = null;
		try {
			Integer result = 0;
			//判断用户是否登录
			String username = sessionProvider.getAttributeForUser
					(RequestUtil.getCSESSIONID(request, response));
			if(StringUtils.isNotBlank(username)){
				result = 1;
			}
			mjv = new MappingJacksonValue(result);
			mjv.setJsonpFunction(callback);
		} catch (Exception e) {
			log.error("isLogin error:{}", e.getMessage(), e);
		}
		log.info("isLogin end.");
		return mjv;
	}
	/**
	 * 提交登录
	 * @param loginDTO
	 * @param model
	 * @return
	 */
	@RequestMapping(value=("/login.aspx"), method=RequestMethod.POST)
	public String login(LoginDTO loginDTO, Model model, HttpServletRequest request, 
			HttpServletResponse response){
		log.info("post login start, loginDTO:{}", ToStringBuilder
				.reflectionToString(loginDTO, ToStringStyle.DEFAULT_STYLE));
		String view = "login";
		try {
			if(StringUtils.isEmpty(loginDTO.getUsername()) &&
					StringUtils.isEmpty(loginDTO.getUsername())){
				model.addAttribute("error", ErrorCodeEnum.LOGIN_INFO_NULL.getMsg());
				return view;
			}
			if(StringUtils.isEmpty(loginDTO.getUsername())){
				model.addAttribute("error", ErrorCodeEnum.USERNAME_CAN_NOT_BE_NULL.getMsg());
				return view;
			}
			if(StringUtils.isEmpty(loginDTO.getPassword())){
				model.addAttribute("error", ErrorCodeEnum.PASSWORD_CAN_NOT_BE_NULL.getMsg());
				return view;
			}
			ResultBase<Buyer> buyerResult = buyerService.
					selectBuyerByUsername(loginDTO.getUsername());
			if(!buyerResult.isSuccess()){
				model.addAttribute("error", ErrorCodeEnum.USER_NOT_EXIST.getMsg());
				return view;
			}
			Buyer buyer = buyerResult.getValue();
			if(!EncodeUtil.encodePassword(loginDTO.getPassword()).equals(buyer.getPassword())){
				model.addAttribute("error", ErrorCodeEnum.PASSWORD_ERROR.getMsg());
				return view;
			}
			//保存用户名到session中
			sessionProvider.setAttributeForUser(RequestUtil.getCSESSIONID
					(request, response), loginDTO.getUsername());
			
			//返回之前访问的页面
			return "redirect:" + (StringUtils.isNotBlank(loginDTO.getReturnUrl()) 
					? loginDTO.getReturnUrl() : "localhost:8082/");
		} catch (BizException e) {
			log.error("post login biz error:{}", e.getMessage(), e);
			model.addAttribute("error", ErrorCodeEnum.PROCESS_DATA_ERROR.getMsg());
		} catch (Exception e) {
			log.error("posg login error:{}", e.getMessage(), e);
			model.addAttribute("error", ErrorCodeEnum.UNKOWN_ERROR.getMsg());
		}
		log.info("post login end.");
		return view;
	}
}
