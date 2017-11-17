package com.caoyong.core.service.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caoyong.common.utlis.EncodeUtil;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.RoleUser;
import com.caoyong.core.bean.system.RoleUserQuery;
import com.caoyong.core.bean.system.User;
import com.caoyong.core.bean.system.UserDTO;
import com.caoyong.core.bean.system.UserQuery;
import com.caoyong.core.bean.system.UserQueryDTO;
import com.caoyong.core.dao.system.RoleUserDao;
import com.caoyong.core.dao.system.UserDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;
import com.caoyong.utils.BeanConvertionHelp;
import com.caoyong.utils.CheckParamsUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 后台用户Service实现
 * 
 * @author caoyong
 * @time 2017年11月16日 上午11:29:06
 */
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao     userDao;
    @Autowired
    private RoleUserDao roleUserDao;

    @Override
    public ResultBase<List<User>> queryUserList(UserQueryDTO query) throws BizException {
        ResultBase<List<User>> result = new ResultBase<>();
        log.info("queryUserList start. query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        try {
            UserQuery example = new UserQuery();
            if (StringUtils.isNotBlank(query.getUsername())) {
                example.createCriteria().andUsernameEqualTo(query.getUsername());
            }
            List<User> users = userDao.selectByExample(example);
            result.setValue(users);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("queryUserList error:", e.getMessage(), e);
        }
        log.info("queryUserList end.");
        return result;
    }

    @Override
    public Page<User> queryUserPage(UserQueryDTO query) throws BizException {
        log.info("queryUserPage start. query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        Page<User> page = new Page<>();
        try {
            UserQuery example = new UserQuery();
            int count = userDao.countByExample(example);
            example.setPageNo(query.getPageNo());
            example.setPageSize(query.getLimit());
            example.setStartRow(query.getStart());
            if (StringUtils.isNotBlank(query.getUsername())) {
                example.createCriteria().andUsernameEqualTo(query.getUsername());
            }
            List<User> rows = userDao.selectByExample(example);
            //设置结果及分页对象
            if (null != rows && !rows.isEmpty()) {
                log.info("queryUserPage results:{}", count);
                log.info("queryUserPage rows:{}",
                        ToStringBuilder.reflectionToString(rows, ToStringStyle.DEFAULT_STYLE));
                page.setStart(query.getStart());
                page.setResults(count);
                page.setLimit(query.getLimit());
                page.setPage(query.getPage());
                page.setPageNo(query.getPageNo());
                page.setRows(rows);
                page.setIsSuccess(true);
            }
            //分页展示
            String url = "/user/userList.do";
            page.pageView(url, null);
        } catch (Exception e) {
            log.error("queryUserPage error:{}", e.getMessage(), e);
            page.setError("数据库查询用户分页失败");
            page.setErrorCode(e.getMessage());
            page.setErrorCode(e.getMessage());
            page.setResults(0);
        }
        log.info("queryUserPage end.");
        return page;
    }

    @Override
    public ResultBase<User> queryUserById(Integer id) throws BizException {
        log.info("queryUserById start id:{}", id);
        ResultBase<User> result = new ResultBase<>();
        try {
            User menu = userDao.selectByPrimaryKey(id);
            result.setSuccess(true);
            result.setValue(menu);
        } catch (Exception e) {
            result.setErrorCode(e.getMessage());
            result.setErrorCode(e.getMessage());
            log.error("queryUserById error:{}", e.getMessage(), e);
        }
        log.info("queryUserById end.");
        return result;
    }

    @Override
    public ResultBase<Integer> updateUserByUserDTO(UserDTO userDTO) throws BizException {
        log.info("updateUserByUserDTO start.menuDTO:{}",
                ToStringBuilder.reflectionToString(userDTO, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        try {
            CheckParamsUtil.check(userDTO, UserDTO.class, "id");
            User record = BeanConvertionHelp.copyBeanFieldValue(User.class, userDTO);
            if (StringUtils.isNotBlank(userDTO.getPassword())) {
                record.setPassword(EncodeUtil.encodePassword(userDTO.getPassword()));
            }
            record.setUpdateTime(new Date());
            int count = userDao.updateByPrimaryKeySelective(record);
            //更新角色用户
            //1.查询出当前用户所有的角色
            RoleUserQuery example = new RoleUserQuery();
            example.createCriteria().andUserIdEqualTo(String.valueOf(userDTO.getId()));
            List<RoleUser> roleUsers = roleUserDao.selectByExample(example);
            //用户所选的角色id
            List<String> roleUserIds = userDTO.getRoleIdList().stream().map(String::valueOf)
                    .collect(Collectors.toList());
            //用户本次要删除的角色
            List<String> delRoleUserIds;
            //用户本次要新增的角色
            List<String> newRoleUserIds = new ArrayList<>();
            if (roleUserIds != null && !roleUserIds.isEmpty()) {
                //2.筛选出所有需要删除的roleUserIds,在用户原来的所有企角色中不包含所选的，即需要删除的
                delRoleUserIds = roleUsers.stream().filter(roleUser -> !roleUserIds.contains(roleUser.getRoleId()))
                        .map(RoleUser::getRoleId).collect(Collectors.toList());
                if (null != delRoleUserIds && !delRoleUserIds.isEmpty()) {
                    UserDTO delRoleUser = new UserDTO();
                    delRoleUser.setId(userDTO.getId());
                    delRoleUser.setIsDeleted(Constants.CONSTANTS_Y);
                    count += updateRoleUserIsDeletedByUserDTO(delRoleUser).getValue();
                }
                //3.所有要新增的roleUserIds，所选角色中在原来角色中找不到的，需要新增
                roleUserIds.stream().forEach(roleId -> {
                    boolean noneMatch = roleUsers.stream().noneMatch(roleUser -> roleUser.getRoleId().equals(roleId));
                    if (noneMatch) {
                        newRoleUserIds.add(roleId);
                    }
                });
                //新增，调用批量插入方法
                if (!newRoleUserIds.isEmpty()) {
                    count += roleUserDao.insertBatch(record);
                }
                //4.找出原来删除过的，这次又需要添加的
                List<String> updateRoleUserIds = roleUsers.stream()
                        .filter(roleUser -> Constants.CONSTANTS_Y.equals(roleUser.getIsDeleted())
                                && roleUserIds.contains(roleUser.getRoleId()))
                        .map(RoleUser::getRoleId).collect(Collectors.toList());
                if (null != updateRoleUserIds && !updateRoleUserIds.isEmpty()) {
                    UserDTO updateRoleUser = new UserDTO();
                    updateRoleUser.setId(userDTO.getId());
                    updateRoleUser.setIsDeleted(Constants.CONSTANTS_N);
                    count += updateRoleUserIsDeletedByUserDTO(updateRoleUser).getValue();
                }
            } else {
                //所有的都不选，删除该用户所有的角色
                if (null != roleUsers && !roleUsers.isEmpty()) {
                    RoleUser roleUserRecord = new RoleUser();
                    roleUserRecord.setIsDeleted(Constants.CONSTANTS_Y);
                    count += roleUserDao.updateByExampleSelective(roleUserRecord, example);
                }
            }
            if (count > 0) {
                result.setValue(count);
            }
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("updateUserByUserDTO Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("updateUserByUserDTO end.");
        return result;
    }

    @Override
    public ResultBase<Integer> saveUserByUserDTO(UserDTO userDTO) throws BizException {
        log.info("saveUserByUserDTO start.menuDTO:{}",
                ToStringBuilder.reflectionToString(userDTO, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        try {
            User record = BeanConvertionHelp.copyBeanFieldValue(User.class, userDTO);
            record.setCreateTime(new Date());
            record.setUpdateTime(new Date());
            record.setPassword(EncodeUtil.encodePassword(userDTO.getPassword()));
            record.setIsDeleted(Constants.CONSTANTS_N);
            record.setModifier(Constants.SYSTEM);
            record.setCreator(Constants.SYSTEM);
            record.setIsEnable(userDTO.isUseable() ? 1 : 0);
            int count = userDao.insertSelective(record);
            //保存角色用户
            count += roleUserDao.insertBatch(record);
            result.setSuccess(true);
            result.setValue(count);
        } catch (Exception e) {
            log.error("saveUserByUserDTO Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("saveUserByUserDTO end.");
        return result;
    }

    @Override
    public ResultBase<Integer> deleteUserById(Integer id) throws BizException {
        log.info("deleteUserById start. id:{}", id);
        ResultBase<Integer> result = new ResultBase<>();
        if (null == id) {
            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL.getMsg(), id + "为空");
        }
        try {
            User record = new User();
            record.setId(id);
            record.setIsDeleted(Constants.CONSTANTS_Y);
            record.setUpdateTime(new Date());
            int count = userDao.updateByPrimaryKeySelective(record);
            if (count > 0) {
                result.setValue(count);
            }
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("deleteUserById Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("deleteUserById end,{} row(s) affected", result.getValue());
        return result;
    }

    @Override
    public ResultBase<Integer> updateRoleUserIsDeletedByUserDTO(UserDTO userDTO) throws BizException {
        log.info("deleteRoleMenuByRoleDTO start.roleDTO:{}",
                ToStringBuilder.reflectionToString(userDTO, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        try {
            int count = 0;
            RoleUserQuery example = new RoleUserQuery();
            List<String> roleIds = userDTO.getRoleIdList().stream().map(String::valueOf).collect(Collectors.toList());
            example.createCriteria().andUserIdEqualTo(String.valueOf(userDTO.getId())).andRoleIdIn(roleIds);
            RoleUser record = new RoleUser();
            record.setIsDeleted(userDTO.getIsDeleted());
            record.setUpdateTime(new Date());
            count += roleUserDao.updateByExampleSelective(record, example);
            result.setSuccess(true);
            result.setValue(count);
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("deleteRoleMenusByRoleDTO error:{}", e.getMessage(), e);
        }
        log.info("deleteRoleMenusByRoleDTO end.");
        return result;
    }

}
