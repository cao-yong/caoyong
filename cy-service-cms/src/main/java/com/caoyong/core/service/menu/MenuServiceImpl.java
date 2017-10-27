package com.caoyong.core.service.menu;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caoyong.core.bean.base.BaseQuery;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.menu.Menu;
import com.caoyong.core.bean.menu.MenuQuery;
import com.caoyong.core.dao.menu.MenuDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

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
    public ResultBase<List<Menu>> queryMenuList() throws BizException {
        log.info("queryMenuList start.");
        ResultBase<List<Menu>> result = new ResultBase<List<Menu>>();
        try {
            BaseQuery query = new BaseQuery();
            List<Menu> menuList = menuDao.selectMenuList(query);
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

    /**
     * 查询菜单分页
     * 
     * @param query 查询条件
     * @return 分页
     */
    @Override
    public Page<Menu> queryMenuPage(BaseQuery query) throws BizException {
        log.info("queryMenuPage start. query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        Page<Menu> page = new Page<>();
        try {
            MenuQuery example = new MenuQuery();
            int count = menuDao.countByExample(example);
            List<Menu> rows = menuDao.selectMenuList(query);
            //设置结果及分页对象
            if (null != rows && !rows.isEmpty()) {
                log.info("queryMenuPage results:{}", count);
                log.info("queryMenuPage rows:{}",
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
            String url = "/menu/menuList.do";
            page.pageView(url, null);
        } catch (Exception e) {
            log.error("queryMenuPage error:{}", e.getMessage(), e);
            page.setError("数据库查询菜单分页失败");
            page.setErrorCode(e.getMessage());
            page.setErrorCode(e.getMessage());
            page.setResults(0);
        }
        log.info("queryMenuPage end.");
        return page;
    }

}
