package com.caoyong.core.service.system;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.caoyong.common.utlis.RandomUUIDUtil;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.Menu;
import com.caoyong.core.bean.system.MenuDTO;
import com.caoyong.core.bean.system.MenuQuery;
import com.caoyong.core.bean.system.MenuQueryDTO;
import com.caoyong.core.bean.system.RoleMenu;
import com.caoyong.core.dao.system.MenuDao;
import com.caoyong.core.dao.system.RoleMenuDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;
import com.caoyong.utils.BeanConvertionHelp;
import com.caoyong.utils.CheckParamsUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单Service
 * 
 * @author caoyong
 * @time 2017年10月26日 下午4:45:49
 */
@Slf4j
@Service(version = "1.0.0")
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao     menuDao;
    @Autowired
    private RoleMenuDao roleMenuDao;

    /**
     * 查询菜单
     * 
     * @param query 查询条件
     * @return 结果集
     */
    @Override
    public ResultBase<List<Menu>> queryMenuList(MenuQueryDTO query) throws BizException {
        log.info("queryMenuList start.query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        ResultBase<List<Menu>> result = new ResultBase<>();
        try {
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
    public Page<Menu> queryMenuPage(MenuQueryDTO query) throws BizException {
        log.info("queryMenuPage start. query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        Page<Menu> page = new Page<>();
        try {
            int count = menuDao.countMenuList();
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

    /**
     * 通过id查询单个菜单
     * 
     * @param id id
     * @return 菜单对象
     * @throws BizException
     */
    @Override
    public ResultBase<Menu> queryMenuById(String id) throws BizException {
        log.info("queryMenuById start id:{}", id);
        ResultBase<Menu> result = new ResultBase<>();
        try {
            Menu menu = menuDao.queryMenuById(id);
            result.setSuccess(true);
            result.setValue(menu);
        } catch (Exception e) {
            result.setErrorCode(e.getMessage());
            result.setErrorCode(e.getMessage());
            log.error("queryMenuById error:{}", e.getMessage(), e);
        }
        log.info("queryMenuById end.");
        return result;
    }

    /**
     * 更新菜单
     * 
     * @param menuDTO 数据传输对象
     * @return 影响的行数
     */
    @Override
    public ResultBase<Integer> updateMenuByMenuDTO(MenuDTO menuDTO) throws BizException {
        log.info("updateMenuByMenuDTO start.menuDTO:{}",
                ToStringBuilder.reflectionToString(menuDTO, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        try {
            CheckParamsUtil.check(menuDTO, MenuDTO.class, "id");
            Menu record = BeanConvertionHelp.copyBeanFieldValue(Menu.class, menuDTO);
            record.setIsShow(menuDTO.getIsShow());
            record.setPermission(menuDTO.getPermissionNew());
            record.setUpdateTime(new Date());
            Menu parent = new Menu();
            parent.setId(menuDTO.getParentId());
            record.setParent(parent);
            int count = menuDao.updateByPrimaryKeySelective(record);
            if (count > 0) {
                result.setValue(count);
            }
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("updateMenuByMenuDTO Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("updateMenuByMenuDTO end.");
        return result;
    }

    /**
     * 保存
     * 
     * @param menuDTO 数据传输对象
     * @return 影响的行数
     */
    @Override
    public ResultBase<Integer> saveMenuAndRoleMenuByMenuDTO(MenuDTO menuDTO) throws BizException {
        log.info("saveMenuByMenuDTO start.menuDTO:{}",
                ToStringBuilder.reflectionToString(menuDTO, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        try {
            Menu record = BeanConvertionHelp.copyBeanFieldValue(Menu.class, menuDTO);
            record.setId(RandomUUIDUtil.getRadomUUID());
            record.setCreateTime(new Date());
            record.setUpdateTime(new Date());
            record.setIsDeleted(Constants.CONSTANTS_N);
            record.setModifier(Constants.SYSTEM);
            record.setCreator(Constants.SYSTEM);
            Menu parent = new Menu();
            parent.setId(menuDTO.getParentId());
            record.setParent(parent);
            menuDao.insertSelective(record);
            //保存role_menu
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(Constants.ONE);
            roleMenu.setMenuId(record.getId());
            roleMenu.setCreateTime(new Date());
            roleMenu.setUpdateTime(new Date());
            roleMenu.setIsDeleted(Constants.CONSTANTS_N);
            roleMenu.setModifier(Constants.SYSTEM);
            roleMenu.setCreator(Constants.SYSTEM);
            roleMenuDao.insertSelective(roleMenu);
            result.setSuccess(true);
            result.setValue(1);
        } catch (Exception e) {
            log.error("saveMenuByMenuDTO Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("saveMenuByMenuDTO end.");
        return result;
    }

    /**
     * 通过id删除菜单
     * 
     * @param id
     * @return
     * @throws BizException
     */
    @Override
    public ResultBase<Integer> deleteMenuById(String id) throws BizException {
        log.info("deleteMenuById start. id:{}", id);
        ResultBase<Integer> result = new ResultBase<>();
        if (StringUtils.isBlank(id)) {
            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL.getMsg(), id + "为空");
        }
        try {
            Menu record = new Menu();
            record.setId(id);
            record.setIsDeleted(Constants.CONSTANTS_Y);
            record.setUpdateTime(new Date());
            int count = menuDao.updateByPrimaryKeySelective(record);
            if (count > 0) {
                result.setValue(count);
            }
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("deleteMenuById Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询已选择过的菜单图标
     * 
     * @return 图标String集合
     */
    @Override
    public ResultBase<List<String>> queryChosenMenuIcons() throws BizException {
        log.info("queryChosenMenuIcon");
        ResultBase<List<String>> result = new ResultBase<>();
        try {
            MenuQuery example = new MenuQuery();
            example.setFields("icon");
            example.setDistinct(true);
            example.createCriteria().andIsDeletedEqualTo(Constants.CONSTANTS_N).andIconIsNotNull()
                    .andIconNotEqualTo("");
            List<Menu> menus = menuDao.selectByExample(example);
            if (menus != null && !menus.isEmpty()) {
                List<String> icons = menus.stream().map(Menu::getIcon).collect(Collectors.toList());
                result.setValue(icons);
                result.setSuccess(true);
                log.info("result:{}", ToStringBuilder.reflectionToString(icons, ToStringStyle.DEFAULT_STYLE));
            }
        } catch (Exception e) {
            result.setErrorCode(e.getMessage());
            result.setErrorCode(e.getMessage());
            log.error("queryChosenMenuIcon error:{}", e.getMessage(), e);
        }
        log.info("queryChosenMenuIcons end.");
        return result;
    }

}
