package com.caoyong.core.service.menu;

import java.util.List;

import com.caoyong.core.bean.base.BaseQuery;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.menu.Menu;
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
    ResultBase<List<Menu>> queryMenuList() throws BizException;

    /**
     * 查询菜单分页
     * 
     * @param query 查询条件
     * @return 分页
     */
    public Page<Menu> queryMenuPage(BaseQuery query) throws BizException;
}
