package com.caoyong.core.controller.menu;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.core.bean.base.BaseQuery;
import com.caoyong.core.bean.base.BaseResponse;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.menu.ChosenMenuIconsResp;
import com.caoyong.core.bean.menu.Menu;
import com.caoyong.core.bean.menu.MenuDTO;
import com.caoyong.core.service.menu.MenuService;
import com.caoyong.enums.ErrorCodeEnum;
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

    /**
     * 跳转编辑菜单视图
     * 
     * @param model
     * @return
     */
    @RequestMapping("/editMenu.do")
    public String editMenu(Model model, String id) {
        log.info("request editMenu start.");
        try {
            ResultBase<Menu> result = menuService.queryMenuById(id);
            if (result.isSuccess()) {
                model.addAttribute("menu", result.getValue());
                model.addAttribute("newtext", false);
            }
        } catch (BizException e) {
            log.error("editMenu BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("editMenu Exception:{}", e.getMessage(), e);
        }
        log.info("request editMenu end.");
        return "/system/menuForm";
    }

    /**
     * 修改菜单
     * 
     * @param menuDTO
     * @return
     */
    @RequestMapping("/modifyMenu.json")
    @ResponseBody
    public BaseResponse modifyMenu(MenuDTO menuDTO) {
        log.info("request modifyMenu start.");
        BaseResponse resp = new BaseResponse();
        try {
            ResultBase<Integer> result = menuService.updateMenuByMenuDTO(menuDTO);
            resp.setCode(result.getErrorCode());
            resp.setMsg(result.getErrorMsg());
            resp.setSuccess(result.isSuccess());
        } catch (BizException e) {
            log.error("modifyMenu BizException:{}", e.getMessage(), e);
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        } catch (Exception e) {
            log.error("modifyMenu Exception:{}", e.getMessage(), e);
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        log.info("request modifyMenu end.");
        return resp;
    }

    /**
     * 删除菜单
     * 
     * @param menuDTO
     * @return
     */
    @RequestMapping("/deleteMenu.json")
    @ResponseBody
    public BaseResponse deleteMenu(String id) {
        log.info("request modifyMenu start.");
        BaseResponse resp = new BaseResponse();
        try {
            ResultBase<Integer> result = menuService.deleteMenuById(id);
            resp.setCode(result.getErrorCode());
            resp.setMsg(result.getErrorMsg());
            resp.setSuccess(result.isSuccess());
        } catch (BizException e) {
            log.error("deleteMenu BizException:{}", e.getMessage(), e);
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        } catch (Exception e) {
            log.error("deleteMenu Exception:{}", e.getMessage(), e);
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        log.info("request deleteMenu end.");
        return resp;
    }

    /**
     * 查询选择过的菜单图标
     * 
     * @param menuDTO
     * @return
     */
    @RequestMapping("/queryChosenMenuIcons.json")
    @ResponseBody
    public ChosenMenuIconsResp queryChosenMenuIcons() {
        log.info("request queryChosenMenuIcons start.");
        ChosenMenuIconsResp resp = new ChosenMenuIconsResp();
        try {
            ResultBase<List<String>> result = menuService.queryChosenMenuIcons();
            resp.setCode(result.getErrorCode());
            resp.setMsg(result.getErrorMsg());
            resp.setSuccess(result.isSuccess());
            if (result.isSuccess()) {
                resp.setIcons(result.getValue());
            }
        } catch (BizException e) {
            log.error("queryChosenMenuIcons BizException:{}", e.getMessage(), e);
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        } catch (Exception e) {
            log.error("queryChosenMenuIcons Exception:{}", e.getMessage(), e);
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        log.info("request queryChosenMenuIcons end.");
        return resp;
    }
}
