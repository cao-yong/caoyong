package com.caoyong.core.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.caoyong.core.bean.TestTb;
import com.caoyong.core.service.TestTbService;

/**
 * 后台管理
 * @author yong.cao
 * @time 2017年6月1日下午10:59:02
 */
@Controller
public class CenterController {
	@Autowired
	private TestTbService testTbService;
	
	@RequestMapping(value="/test/index.do")
	public String index(Model model){
		TestTb testTb = new TestTb();
		testTb.setBirthday(new Date());
		testTb.setName("李冰冰");
		testTbService.insertTestTb(testTb);
		return "index";
	}
}
