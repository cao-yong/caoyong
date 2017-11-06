package com.caoyong.core.controller.system;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.utlis.JSONConversionUtil;
import com.caoyong.core.bean.base.BaseQuery;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.Menu;
import com.caoyong.core.bean.system.MenuQueryDTO;
import com.caoyong.core.bean.system.MenuTreeVO;
import com.caoyong.core.bean.system.Role;
import com.caoyong.core.service.system.MenuService;
import com.caoyong.core.service.system.RoleService;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 角色控制层
 * 
 * @author caoyong
 * @time 2017年11月3日 下午4:49:10
 */
@Controller
@RequestMapping("/role")
@Slf4j
public class RoleController {
    @Reference(version = "1.0.0", timeout = 3000000)
    private RoleService roleService;

    @Reference(version = "1.0.0", timeout = 3000000)
    private MenuService menuService;

    /**
     * 跳转角色列表视图
     * 
     * @param model
     * @return
     */
    @RequestMapping("/roleList.do")
    public String roleList(Model model, BaseQuery query) {
        log.info("request menuList start.");
        try {
            query.setPage(true);
            query.setLimit(query.getLimit());
            Page<Role> page = roleService.queryRolePage(query);
            model.addAttribute("page", page);
        } catch (BizException e) {
            log.error("menuList BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("menuList Exception:{}", e.getMessage(), e);
        }
        log.info("request menuList end.");
        return "/system/roleList";
    }

    /**
     * 新增角色
     * 
     * @param model
     * @return
     */
    @RequestMapping("/newRole.do")
    public String newRole(Model model) {
        log.info("request newRole start.");
        try {
            MenuQueryDTO query = new MenuQueryDTO();
            ResultBase<List<Menu>> result = menuService.queryMenuList(query);
            if (result.isSuccess()) {
                model.addAttribute("menus", result.getValue());
                if (result.getValue().size() > 0) {
                    List<MenuTreeVO> menuTrees = result.getValue().stream().map(MenuTreeVO::new)
                            .collect(Collectors.toList());
                    //把menuTrees转成json字符串
                    String menus = JSONConversionUtil.objToString(menuTrees);
                    model.addAttribute("menus", menus);
                    log.info("convert result menu:{}", menus);
                }
            }
            model.addAttribute("role", new Role());
        } catch (BizException e) {
            log.error("newRole BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("newRole Exception:{}", e.getMessage(), e);
        }
        log.info("request newRole end.");
        return "/system/roleForm";
    }
}
