package com.caoyong.core.controller.system;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.core.bean.base.BaseResponse;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.Role;
import com.caoyong.core.bean.system.RoleQueryDTO;
import com.caoyong.core.bean.system.User;
import com.caoyong.core.bean.system.UserDTO;
import com.caoyong.core.bean.system.UserQueryDTO;
import com.caoyong.core.service.system.RoleService;
import com.caoyong.core.service.system.UserService;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户管理
 * 
 * @author caoyong
 * @time 2017年11月15日 下午4:38:48
 */
@Slf4j
@RequestMapping("/user")
@Controller
public class UserController {
    @Reference(version = "1.0.0", timeout = 3000000)
    private UserService userService;
    @Reference(version = "1.0.0", timeout = 3000000)
    private RoleService roleService;

    @RequestMapping("/userList.do")
    public String userList(Model model, UserQueryDTO query) {
        log.info("request userList start,query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        try {
            Page<User> page = userService.queryUserPage(query);
            model.addAttribute("page", page);
        } catch (BizException e) {
            log.error("userList BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("userList Exception:{}", e.getMessage(), e);
        }
        log.info("request userList end.");
        return "/system/userList";
    }

    @RequestMapping("/newUser.do")
    public String newUser(Model model) {
        log.info("request newUser start.");
        try {
            RoleQueryDTO query = new RoleQueryDTO();
            ResultBase<List<Role>> result = roleService.queryRoleList(query);
            if (result.isSuccess() && !result.getValue().isEmpty()) {
                model.addAttribute("roles", result.getValue());
            }
        } catch (BizException e) {
            log.error("request newUser BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("request newUser Exception:{}", e.getMessage(), e);
        }
        log.info("request newUser end.");
        model.addAttribute("user", new User());
        return "/system/userForm";
    }

    @RequestMapping("/isExistUser.json")
    @ResponseBody
    public BaseResponse isExistUser(UserQueryDTO query) {
        BaseResponse resp = new BaseResponse();
        log.info("request isExistUser start, username:{}", query.getUsername());
        try {
            ResultBase<List<User>> users = userService.queryUserList(query);
            if (users.isSuccess() && null != users.getValue() && !users.getValue().isEmpty()) {
                resp.setMsg(ErrorCodeEnum.USER_EXIST.getMsg());
                resp.setCode(ErrorCodeEnum.USER_EXIST.getCode());
            }
            if (users.isSuccess() && users.getValue() == null) {
                resp.setSuccess(true);
            }
        } catch (BizException e) {
            log.error("request isExistUser BizException:{}", e.getMessage(), e);
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        } catch (Exception e) {
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("request isExistUser Exception:{}", e.getMessage(), e);
        }
        log.info("request isExistUser end.");
        return resp;
    }

    @RequestMapping("/saveUser.json")
    @ResponseBody
    public BaseResponse saveUser(UserDTO userDTO) {
        BaseResponse resp = new BaseResponse();
        log.info("request saveUser start, user:{}",
                ToStringBuilder.reflectionToString(userDTO, ToStringStyle.DEFAULT_STYLE));
        try {
            ResultBase<Integer> users = userService.saveUserByUserDTO(userDTO);
            if (users.isSuccess() && users.getValue() > 0) {
                resp.setSuccess(true);
            }
        } catch (BizException e) {
            log.error("request saveUser BizException:{}", e.getMessage(), e);
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        } catch (Exception e) {
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("request saveUser Exception:{}", e.getMessage(), e);
        }
        log.info("request saveUser end.");
        return resp;
    }
}
