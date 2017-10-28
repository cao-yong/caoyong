package com.caoyong.core.controller.menu;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.core.bean.base.BaseQuery;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.menu.Menu;
import com.caoyong.core.service.menu.MenuService;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单
 * 
 * @author caoyong
 * @time 2017年10月27日 下午6:33:16
 */
@Controller
@RequestMapping("/menu")
@Slf4j
public class MenuController {
    @Reference(version = "1.0.0")
    private MenuService menuService;

    /**
     * 跳转菜单视图
     * 
     * @param model
     * @return
     */
    @RequestMapping("/menuList.do")
    public String menuList(Model model, BaseQuery query) {
        log.info("request menuList start.");
        try {
        	query.setPage(true);
        	query.setLimit(query.getLimit());
            Page<Menu> page = menuService.queryMenuPage(query);
            model.addAttribute("page", page);
        } catch (BizException e) {
            log.error("menuList BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("menuList Exception:{}", e.getMessage(), e);
        }
        log.info("request menuList end.");
        return "/system/menuList";
    }

}
