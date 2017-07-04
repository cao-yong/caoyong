package com.caoyong.core.controller.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 前台商品
 * @author yong.cao
 * @time 2017年7月2日下午3:33:18
 */
@Controller
public class ProductController {
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping(value=("/"))
	public String index(){
		return "index";
	}
}
