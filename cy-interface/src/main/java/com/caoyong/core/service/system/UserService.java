package com.caoyong.core.service.system;

import java.util.List;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.User;
import com.caoyong.core.bean.system.UserDTO;
import com.caoyong.core.bean.system.UserQueryDTO;
import com.caoyong.exception.BizException;

/**
 * 后台用户Service
 * 
 * @author caoyong
 * @ 2017年11月16日 上午11:17:31
 */
public interface UserService {
    /**
     * 查询用户
     * 
     * @param query 查询条件
     * @return 结果集
     * @throws BizException 业务异常
     */
    ResultBase<List<User>> queryUserList(UserQueryDTO query) throws BizException;

    /**
     * 查询用户分页
     * 
     * @param query 查询条件
     * @return 分页
     * @throws BizException 业务异常
     */
    Page<User> queryUserPage(UserQueryDTO query) throws BizException;

    /**
     * 通过id查询单个用户
     * 
     * @param id id
     * @return 用户对象
     * @throws BizException 业务异常
     */
    ResultBase<User> queryUserById(Integer id) throws BizException;

    /**
     * 更新用户
     * 
     * @param userDTO 数据传输对象
     * @return 影响的行数
     * @throws BizException 业务异常
     */
    ResultBase<Integer> updateUserByUserDTO(UserDTO userDTO) throws BizException;

    /**
     * 保存用户
     * 
     * @param userDTO 数据传输对象
     * @return 影响的行数
     * @throws BizException 业务异常
     */
    ResultBase<Integer> saveUserByUserDTO(UserDTO userDTO) throws BizException;

    /**
     * 通过id删除用户
     * 
     * @param id id
     * @return 影响行数
     * @throws BizException 业务异常
     */
    ResultBase<Integer> deleteUserById(Integer id) throws BizException;

    /**
     * 通过id停用、启用用户
     * 
     * @param id id
     * @return 影响行数
     * @throws BizException 业务异常
     */
    ResultBase<Integer> enableUserById(Integer id) throws BizException;

    /**
     * 删除或恢复角色用户
     * 
     * @param userDTO 用户数据对象
     * @return 影响行数
     * @throws BizException 业务异常
     */
    ResultBase<Integer> updateRoleUserIsDeletedByUserDTO(UserDTO userDTO) throws BizException;

    /**
     * 根据id查询用户的角色
     * 
     * @param id id
     * @return 用户
     * @throws BizException 业务异常
     */
    ResultBase<User> queryUserRoleById(Integer id) throws BizException;

    /**
     * 通过username查询单个用户及所有角色
     *
     * @param username username
     * @return 用户对象
     * @throws BizException 业务异常
     */
    ResultBase<User> queryUserAndRolesByUsername(String username) throws BizException;

    /**
     * 更新用户登录时间
     * @param userId 用户id
     * @return 影响行数
     * @throws BizException 业务异常
     */
    ResultBase<Integer> updateUserOperateDateByUserId(Integer userId) throws BizException;
}
