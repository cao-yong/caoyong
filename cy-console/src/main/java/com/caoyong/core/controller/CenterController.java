package com.caoyong.core.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.utlis.DateUtil;
import com.caoyong.common.utlis.SystemInfoUtil;
import com.caoyong.common.vo.SystemInfo;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.statistics.VisitsStatistics;
import com.caoyong.core.bean.system.Menu;
import com.caoyong.core.bean.system.User;
import com.caoyong.core.service.statistics.VisitsService;
import com.caoyong.core.service.system.MenuService;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 后台管理
 * 
 * @author yong.cao
 * @since 2017年6月1日下午10:59:02
 */

@RequestMapping(value = "/control")
@Controller
@Slf4j
public class CenterController {
    @Reference(version = "1.0.0")
    private MenuService   menuService;

    @Reference(version = "1.0.0")
    private VisitsService visitsService;

    @RequestMapping(value = "/index.do")
    public String index(Model model, HttpServletRequest request) {
        log.info("index start");
        try {
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("currentUser");
            //查询出页面的菜单
            ResultBase<List<Menu>> result = menuService.queryMenuListByUser(currentUser);
            List<Menu> menus = result.getValue();
            model.addAttribute("menus", menus);
            model.addAttribute("currentUser", currentUser);
            //查询系统基本信息
            SystemInfo systemInfo = SystemInfoUtil.getSystemInfo();
            if (null != systemInfo) {
                String operateDate = DateUtil.formatDate(currentUser.getOperateDate(), DateUtil.ZH_CN_DATETIME_PATTERN);
                systemInfo.setOperateDate(operateDate);
            }
            model.addAttribute("systemInfo", systemInfo);
            ResultBase<List<VisitsStatistics>> visitsResultBase = visitsService.selectVisitsStatistics();
            if (visitsResultBase.isSuccess() && !visitsResultBase.getValue().isEmpty()) {
                model.addAttribute("visitsStatistics", visitsResultBase.getValue());
            }
        } catch (BizException e) {
            log.error("index BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("index Exception:{}", e.getMessage(), e);
        }
        log.info("index end.");
        return "index";
    }

    @RequestMapping(value = { "/login.do" })
    public String login() {
        return "/system/login";
    }
}
