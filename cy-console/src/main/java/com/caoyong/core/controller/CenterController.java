package com.caoyong.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理
 * @author yong.cao
 * @time 2017年6月1日下午10:59:02
 */
@Controller
public class CenterController {
	@RequestMapping(value="/test/index.do")
	public String index(Model model){
		return "index";
	}
}
