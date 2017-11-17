package com.caoyong.core.service.system;

import java.util.List;

import com.caoyong.core.bean.base.BaseQuery;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.Role;
import com.caoyong.core.bean.system.RoleDTO;
import com.caoyong.core.bean.system.RoleQueryDTO;
import com.caoyong.exception.BizException;

/**
 * 角色Service
 * 
 * @author caoyong
 * @time 2017年11月3日 下午4:52:15
 */
public interface RoleService {
    /**
     * 查询角色
     * 
     * @param query 查询条件
     * @return 结果集
     */
    ResultBase<List<Role>> queryRoleList(RoleQueryDTO query) throws BizException;

    /**
     * 查询角色分页
     * 
     * @param query 查询条件
     * @return 分页
     */
    public Page<Role> queryRolePage(BaseQuery query) throws BizException;

    /**
     * 通过id查询单个角色
     * 
     * @param id id
     * @return 角色对象
     * @throws BizException
     */
    ResultBase<Role> queryRoleById(Long id) throws BizException;

    /**
     * 通过id查询单个角色下的所有菜单
     * 
     * @param id id
     * @return 角色对象
     * @throws BizException
     */
    ResultBase<Role> selectRoleMenusByRoleId(String roleId) throws BizException;

    /**
     * 更新角色
     * 
     * @param RoleDTO 数据传输对象
     * @return 影响的行数
     */
    ResultBase<Integer> updateRoleByRoleDTO(RoleDTO roleDTO) throws BizException;

    /**
     * 保存角色
     * 
     * @param RoleDTO 数据传输对象
     * @return 影响的行数
     * @throws BizException
     */
    ResultBase<Integer> saveRoleByRoleDTO(RoleDTO roleDTO) throws BizException;

    /**
     * 通过id删除角色
     * 
     * @param id
     * @return
     * @throws BizException
     */
    ResultBase<Integer> deleteRoleById(Long id) throws BizException;

    /**
     * 删除或恢复角色菜单
     * 
     * @param roleDTO
     * @return
     * @throws BizException
     */
    ResultBase<Integer> updateRoleMenusIsDeletedByRoleDTO(RoleDTO roleDTO) throws BizException;
}
