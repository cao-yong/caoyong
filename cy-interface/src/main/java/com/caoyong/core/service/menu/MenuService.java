package com.caoyong.core.service.menu;

import java.util.List;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.menu.Menu;
import com.caoyong.core.bean.menu.MenuDTO;
import com.caoyong.core.bean.menu.MenuQueryDTO;
import com.caoyong.exception.BizException;

/**
 * 菜单Service
 * 
 * @author caoyong
 * @time 2017年10月26日 下午4:45:49
 */
public interface MenuService {
    /**
     * 查询菜单
     * 
     * @param query 查询条件
     * @return 结果集
     */
    ResultBase<List<Menu>> queryMenuList(MenuQueryDTO query) throws BizException;

    /**
     * 查询菜单分页
     * 
     * @param query 查询条件
     * @return 分页
     */
    public Page<Menu> queryMenuPage(MenuQueryDTO query) throws BizException;

    /**
     * 通过id查询单个菜单
     * 
     * @param id id
     * @return 菜单对象
     * @throws BizException
     */
    ResultBase<Menu> queryMenuById(String id) throws BizException;

    /**
     * 更新菜单
     * 
     * @param menuDTO 数据传输对象
     * @return 影响的行数
     */
    ResultBase<Integer> updateMenuByMenuDTO(MenuDTO menuDTO) throws BizException;

    /**
     * 保存菜单
     * 
     * @param menuDTO 数据传输对象
     * @return 影响的行数
     * @throws BizException
     */
    ResultBase<Integer> saveMenuAndRoleMenuByMenuDTO(MenuDTO menuDTO) throws BizException;

    /**
     * 通过id删除菜单
     * 
     * @param id
     * @return
     * @throws BizException
     */
    ResultBase<Integer> deleteMenuById(String id) throws BizException;

    /**
     * 查询已选择过的菜单图标
     * 
     * @return 图标String集合
     */
    ResultBase<List<String>> queryChosenMenuIcons() throws BizException;
}
