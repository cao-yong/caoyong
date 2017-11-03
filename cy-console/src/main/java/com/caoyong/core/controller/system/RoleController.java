package com.caoyong.core.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.core.bean.base.BaseQuery;
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
            roleService.queryRolePage(query);
        } catch (BizException e) {
            log.error("menuList BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("menuList Exception:{}", e.getMessage(), e);
        }
        log.info("request menuList end.");
        return "/system/roleList";
    }
}
