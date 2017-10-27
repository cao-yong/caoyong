package com.caoyong.core.service.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.menu.Menu;
import com.caoyong.core.dao.menu.MenuDao;
import com.caoyong.enums.ErrorCodeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单Service
 * 
 * @author caoyong
 * @time 2017年10月26日 下午4:45:49
 */
@Slf4j
@Service("menuService")
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    /**
     * 查询菜单
     * 
     * @param query 查询条件
     * @return 结果集
     */
    @Override
    public ResultBase<List<Menu>> queryMenuList() {
        log.info("queryMenuList start.");
        ResultBase<List<Menu>> result = new ResultBase<List<Menu>>();
        try {
            List<Menu> menuList = menuDao.selectMenuList();
            result.setValue(menuList);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("queryMenuList error:", e.getMessage(), e);
        }
        log.info("queryMenuList end.");
        return result;
    }

}
