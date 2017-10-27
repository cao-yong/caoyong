package com.caoyong.core.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.utlis.SystemInfoUtil;
import com.caoyong.common.vo.SystemInfo;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.menu.Menu;
import com.caoyong.core.service.menu.MenuService;

import lombok.extern.slf4j.Slf4j;

/**
 * 后台管理
 * 
 * @author yong.cao
 * @time 2017年6月1日下午10:59:02
 */

@RequestMapping(value = "/control")
@Controller
@Slf4j
public class CenterController {
    @Reference(version = "1.0.0")
    private MenuService menuService;

    @RequestMapping(value = "/index.do")
    public String index(Model model) {
        log.info("index start");
        //查询出页面的菜单
        try {
            ResultBase<List<Menu>> result = menuService.queryMenuList();
            List<Menu> menus = result.getValue();
            model.addAttribute("menus", menus);
            //查询系统基本信息
            SystemInfo systemInfo = SystemInfoUtil.getSystemInfo();
            model.addAttribute("systemInfo", systemInfo);
        } catch (Exception e) {
            log.error("index Exception:{}", e.getMessage(), e);
        }
        log.info("index end.");
        return "index";
    }

    @RequestMapping(value = "/top.do")
    public String top(Model model) {
        return "top";
    }

    @RequestMapping(value = "/main.do")
    public String main(Model model) {
        return "main";
    }

    @RequestMapping(value = "/left.do")
    public String left(Model model) {
        return "left";
    }

    @RequestMapping(value = "/right.do")
    public String right(Model model) {
        return "right";
    }

    @RequestMapping(value = "/frame/product_main.do")
    public String product_main(Model model) {
        return "frame/product_main";
    }

    @RequestMapping(value = "/frame/product_left.do")
    public String product_left(Model model) {
        return "frame/product_left";
    }
}
