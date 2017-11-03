package com.caoyong.core.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.utlis.SystemInfoUtil;
import com.caoyong.common.vo.SystemInfo;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.Menu;
import com.caoyong.core.bean.system.MenuQueryDTO;
import com.caoyong.core.service.system.MenuService;
import com.caoyong.exception.BizException;

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
            MenuQueryDTO query = new MenuQueryDTO();
            ResultBase<List<Menu>> result = menuService.queryMenuList(query);
            List<Menu> menus = result.getValue();
            model.addAttribute("menus", menus);
            //查询系统基本信息
            SystemInfo systemInfo = SystemInfoUtil.getSystemInfo();
            model.addAttribute("systemInfo", systemInfo);
        } catch (BizException e) {
            log.error("index BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("index Exception:{}", e.getMessage(), e);
        }
        log.info("index end.");
        return "index";
    }
}
