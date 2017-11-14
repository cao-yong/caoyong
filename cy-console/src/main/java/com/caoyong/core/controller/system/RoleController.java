package com.caoyong.core.controller.system;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.utlis.JSONConversionUtil;
import com.caoyong.core.bean.base.BaseQuery;
import com.caoyong.core.bean.base.BaseResponse;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.Menu;
import com.caoyong.core.bean.system.MenuQueryDTO;
import com.caoyong.core.bean.system.MenuTreeVO;
import com.caoyong.core.bean.system.Role;
import com.caoyong.core.bean.system.RoleDTO;
import com.caoyong.core.bean.system.RoleQueryDTO;
import com.caoyong.core.service.system.MenuService;
import com.caoyong.core.service.system.RoleService;
import com.caoyong.enums.ErrorCodeEnum;
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

    /**
     * 编辑角色
     * 
     * @param model
     * @return
     */
    @RequestMapping("/editRole.do")
    public String editRole(Model model, String roleId) {
        log.info("request editRole start.");
        try {
            //查询角色下的所有菜单
            ResultBase<Role> result = roleService.selectRoleMenusByRoleId(roleId);
            if (result.isSuccess()) {
                List<Menu> roleMenus = result.getValue().getMenuList();
                model.addAttribute("role", result.getValue());
                //查询所有的菜单
                MenuQueryDTO query = new MenuQueryDTO();
                ResultBase<List<Menu>> menuResult = menuService.queryMenuList(query);
                if (menuResult.getValue().size() > 0) {
                    List<MenuTreeVO> menuTrees = menuResult.getValue().stream().map(menu -> {
                        MenuTreeVO treeVO = new MenuTreeVO();
                        treeVO.setId(menu.getId());
                        treeVO.setName(menu.getName());
                        treeVO.setpId(menu.getParentId());
                        treeVO.setOpen(true);
                        treeVO.setChecked(roleMenus.contains(menu));
                        return treeVO;
                    }).collect(Collectors.toList());
                    //把menuTrees转成json字符串
                    String menus = JSONConversionUtil.objToString(menuTrees);
                    model.addAttribute("menus", menus);
                    log.info("convert result menu:{}", menus);
                }
            }
        } catch (BizException e) {
            log.error("editRole BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("editRole Exception:{}", e.getMessage(), e);
        }
        log.info("request editRole end.");
        return "/system/roleForm";
    }

    /**
     * 删除角色
     * 
     * @param id
     * @return
     */
    @RequestMapping("/deleteRole.json")
    @ResponseBody
    public BaseResponse deleteRole(Long id) {
        log.info("request deleteRole start.");
        BaseResponse resp = new BaseResponse();
        try {
            ResultBase<Integer> result = roleService.deleteRoleById(id);
            resp.setCode(result.getErrorCode());
            resp.setMsg(result.getErrorMsg());
            resp.setSuccess(result.isSuccess());
        } catch (BizException e) {
            log.error("deleteRole BizException:{}", e.getMessage(), e);
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        } catch (Exception e) {
            log.error("deleteRole Exception:{}", e.getMessage(), e);
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        log.info("request deleteRole end.");
        return resp;
    }

    /**
     * 保存角色
     * 
     * @param model
     * @return
     */
    @RequestMapping("/saveRole.json")
    @ResponseBody
    public BaseResponse saveRole(RoleDTO roleDTO, Model model) {
        log.info("request saveRole start.");
        BaseResponse resp = new BaseResponse();
        try {
            RoleQueryDTO query = new RoleQueryDTO();
            query.setName(roleDTO.getName());
            ResultBase<List<Role>> result = roleService.queryRoleList(query);
            if (result.isSuccess() && !result.getValue().isEmpty() && roleDTO.getId() == null) {
                resp.setMsg("角色已存在");
                return resp;
            }
            ResultBase<Integer> roleResult;
            if (roleDTO.getId() == null) {
                roleDTO.setIsEnable(1);
                roleResult = roleService.saveRoleByRoleDTO(roleDTO);
            } else {
                roleResult = roleService.updateRoleByRoleDTO(roleDTO);
            }
            if (roleResult.isSuccess() && roleResult.getValue() > 0) {
                resp.setSuccess(true);
                resp.setMsg("保存角色成功");
            }
        } catch (BizException e) {
            log.error("saveRole BizException:{}", e.getMessage(), e);
            resp.setMsg("保存角色失败，业务处理异常");
        } catch (Exception e) {
            log.error("saveRole Exception:{}", e.getMessage(), e);
            resp.setMsg("保存角色失败，未知错误");
        }
        log.info("request saveRole end.");
        return resp;
    }
}
