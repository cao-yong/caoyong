package com.caoyong.core.controller.system;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.utlis.JSONConversionUtil;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.BaseResponse;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.ChosenMenuIconsResp;
import com.caoyong.core.bean.system.Menu;
import com.caoyong.core.bean.system.MenuDTO;
import com.caoyong.core.bean.system.MenuQueryDTO;
import com.caoyong.core.bean.system.MenuTreeNodeVO;
import com.caoyong.core.bean.system.MenuTreeVO;
import com.caoyong.core.service.system.MenuService;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单
 * 
 * @author caoyong
 * @ 2017年10月27日 下午6:33:16
 */
@Controller
@RequestMapping("/menu")
@Slf4j
public class MenuController {
    @Reference(version = "1.0.0", timeout = 3000000)
    private MenuService menuService;

    /**
     * 跳转菜单视图
     * 
     * @param model 数据模型
     * @return 视图名称
     */
    @RequestMapping("/menuList.do")
    public String menuList(Model model, MenuQueryDTO query) {
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
     * @param model 数据模型
     * @return 视图名称
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
     * 获取父节点信息
     * @param name 名称
     * @return 节点实体
     */
    @ResponseBody
    @RequestMapping("/getParent.json")
    public MenuTreeNodeVO getParent(String name) {
        log.info("request getParent start.");
        MenuTreeNodeVO vo = new MenuTreeNodeVO();
        try {
            MenuQueryDTO query = new MenuQueryDTO();
            query.setName(name);
            ResultBase<List<Menu>> result = menuService.queryMenuList(query);
            if (result.isSuccess() && !result.getValue().isEmpty()) {
                Menu menu = result.getValue().get(0);
                vo.setMsg(menu);
                ResultBase<List<String>> icons = menuService.queryChosenMenuIcons();
                if (icons.isSuccess()) {
                    vo.setIcons(icons.getValue());
                }
                vo.setCode(Constants.ONE);
            }
        } catch (BizException e) {
            log.error("getParent BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("getParent Exception:{}", e.getMessage(), e);
        }
        log.info("request getParent end.");
        return vo;
    }

    /**
     * 跳转新建菜单视图
     * 
     * @param model 数据模型
     * @return 数据实体
     */
    @RequestMapping("/newMenu.do")
    public String newMenu(Model model) {
        log.info("request newMenu start.");
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
                model.addAttribute("newtext", true);
                Menu menu = new Menu();
                menu.setIsShow(1);
                menu.setIsDeleted(Constants.CONSTANTS_N);
                menu.setParent(new Menu());
                model.addAttribute("menu", menu);
            }
        } catch (BizException e) {
            log.error("newMenu BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("newMenu Exception:{}", e.getMessage(), e);
        }
        log.info("request newMenu end.");
        return "/system/menuForm";
    }

    /**
     * 修改菜单
     * 
     * @param menuDTO 菜单传输对象
     * @return 结果
     */
    @RequestMapping("/modifyMenu.json")
    @ResponseBody
    public BaseResponse modifyMenu(MenuDTO menuDTO) {
        log.info("request modifyMenu start.");
        BaseResponse resp = new BaseResponse();
        try {
            ResultBase<Integer> result;
            if (StringUtils.isNotBlank(menuDTO.getId())) {
                result = menuService.updateMenuByMenuDTO(menuDTO);
            } else {
                result = menuService.saveMenuAndRoleMenuByMenuDTO(menuDTO);
            }
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
     * @param id id
     * @return 结果
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
     * @return 已选择过的图标
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
