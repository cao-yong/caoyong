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
 * @time 2017年11月16日 上午11:17:31
 */
public interface UserService {
    /**
     * 查询用户
     * 
     * @param query 查询条件
     * @return 结果集
     */
    ResultBase<List<User>> queryUserList(UserQueryDTO query) throws BizException;

    /**
     * 查询用户分页
     * 
     * @param query 查询条件
     * @return 分页
     */
    public Page<User> queryUserPage(UserQueryDTO query) throws BizException;

    /**
     * 通过id查询单个用户
     * 
     * @param id id
     * @return 用户对象
     * @throws BizException
     */
    ResultBase<User> queryUserById(Integer id) throws BizException;

    /**
     * 更新用户
     * 
     * @param userDTO 数据传输对象
     * @return 影响的行数
     */
    ResultBase<Integer> updateUserByUserDTO(UserDTO userDTO) throws BizException;

    /**
     * 保存用户
     * 
     * @param userDTO 数据传输对象
     * @return 影响的行数
     * @throws BizException
     */
    ResultBase<Integer> saveUserByUserDTO(UserDTO userDTO) throws BizException;

    /**
     * 通过id删除用户
     * 
     * @param id
     * @return
     * @throws BizException
     */
    ResultBase<Integer> deleteUserById(Integer id) throws BizException;

    /**
     * 删除或恢复角色用户
     * 
     * @param userDTO
     * @return
     * @throws BizException
     */
    ResultBase<Integer> updateRoleUserIsDeletedByUserDTO(UserDTO userDTO) throws BizException;

}
