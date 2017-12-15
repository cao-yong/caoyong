package com.caoyong.core.service.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.BaseQuery;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.Role;
import com.caoyong.core.bean.system.RoleDTO;
import com.caoyong.core.bean.system.RoleMenu;
import com.caoyong.core.bean.system.RoleMenuBatchDO;
import com.caoyong.core.bean.system.RoleMenuQuery;
import com.caoyong.core.bean.system.RoleQuery;
import com.caoyong.core.bean.system.RoleQuery.Criteria;
import com.caoyong.core.bean.system.RoleQueryDTO;
import com.caoyong.core.dao.system.RoleDao;
import com.caoyong.core.dao.system.RoleMenuDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;
import com.caoyong.utils.BeanConvertionHelp;
import com.caoyong.utils.CheckParamsUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 角色实现
 * 
 * @author caoyong
 * @time 2017年11月3日 下午5:05:29
 */
@Service(version = "1.0.0")
//@Transactional(rollbackFor = Exception.class)
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao     roleDao;
    @Autowired
    private RoleMenuDao roleMenuDao;

    @Override
    public ResultBase<List<Role>> queryRoleList(RoleQueryDTO query) throws BizException {
        log.info("queryRoleList start. query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        ResultBase<List<Role>> result = new ResultBase<>();
        try {
            RoleQuery example = new RoleQuery();
            Criteria criteria = example.createCriteria().andIsDeletedEqualTo(Constants.CONSTANTS_N);
            if (StringUtils.isNotBlank(query.getName())) {
                criteria.andNameEqualTo(query.getName());
            }
            List<Role> roleList = roleDao.selectByExample(example);
            result.setValue(roleList);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("queryRoleList error:", e.getMessage(), e);
        }
        log.info("queryRoleList end.");
        return result;
    }

    @Override
    public Page<Role> queryRolePage(BaseQuery query) throws BizException {
        log.info("queryRolePage start. query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        Page<Role> page = new Page<>();
        try {
            RoleQuery example = new RoleQuery();
            int count = roleDao.countByExample(example);
            example.setPageNo(query.getPageNo());
            example.setPageSize(query.getLimit());
            example.createCriteria().andIsDeletedEqualTo(Constants.CONSTANTS_N);
            List<Role> rows = roleDao.selectByExample(example);
            //设置结果及分页对象
            if (null != rows && !rows.isEmpty()) {
                log.info("queryRolePage results:{}", count);
                log.info("queryRolePage rows:{}",
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
            String url = "/role/roleList.do";
            page.pageView(url, null);
        } catch (Exception e) {
            log.error("queryRolePage error:{}", e.getMessage(), e);
            page.setError("数据库查询角色分页失败");
            page.setErrorCode(e.getMessage());
            page.setErrorCode(e.getMessage());
            page.setResults(0);
        }
        log.info("queryRolePage end.");
        return page;
    }

    @Override
    public ResultBase<Role> queryRoleById(Long id) throws BizException {
        log.info("queryRoleById start id:{}", id);
        ResultBase<Role> result = new ResultBase<>();
        try {
            Role role = roleDao.selectByPrimaryKey(id);
            result.setSuccess(true);
            result.setValue(role);
        } catch (Exception e) {
            result.setErrorCode(e.getMessage());
            result.setErrorCode(e.getMessage());
            log.error("queryRoleById error:{}", e.getMessage(), e);
        }
        log.info("queryRoleById end.");
        return result;
    }

    @Override
    public ResultBase<Integer> updateRoleByRoleDTO(RoleDTO roleDTO) throws BizException {
        log.info("updateRoleByRoleDTO start.menuDTO:{}",
                ToStringBuilder.reflectionToString(roleDTO, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        try {
            CheckParamsUtil.check(roleDTO, RoleDTO.class, "id");
            Role record = BeanConvertionHelp.copyBeanFieldValue(Role.class, roleDTO);
            record.setUpdateTime(new Date());

            int count = roleDao.updateByPrimaryKeySelective(record);
            //编辑角色菜单
            //先查询出该角色已有的菜单
            RoleMenuQuery example = new RoleMenuQuery();
            example.createCriteria().andRoleIdEqualTo(String.valueOf(roleDTO.getId()));
            List<RoleMenu> roleMenus = roleMenuDao.selectByExample(example);
            String[] ids = new String[0];
            if (StringUtils.isNotBlank(roleDTO.getMenuIds())) {
                ids = roleDTO.getMenuIds().split(",");
            }
            List<String> roleMenuIds = Arrays.asList(ids);
            List<String> delRoleMenuIds;
            List<String> newRoleMenuIds = new ArrayList<>();
            if (roleMenuIds != null && !roleMenuIds.isEmpty()) {
                //筛选出所有需要删除的roleMenuIds,在用户原来的所有菜单中不包含所选的，即需要删除的
                delRoleMenuIds = roleMenus.stream().filter(roleMenu -> !roleMenuIds.contains(roleMenu.getMenuId()))
                        .map(RoleMenu::getMenuId).collect(Collectors.toList());
                if (null != delRoleMenuIds && !delRoleMenuIds.isEmpty()) {
                    RoleDTO delRoleMenu = new RoleDTO();
                    delRoleMenu.setId(roleDTO.getId());
                    delRoleMenu.setIsDeleted(Constants.CONSTANTS_Y);
                    delRoleMenu.setRoleMenuIds(delRoleMenuIds);
                    count += updateRoleMenusIsDeletedByRoleDTO(delRoleMenu).getValue();
                }
                //所有要新增的roleMenuIds，所选菜单中在原来菜单中找不到的，需要新增
                roleMenuIds.stream().forEach(menuId -> {
                    boolean noneMatch = roleMenus.stream().noneMatch(roleMenu -> roleMenu.getMenuId().equals(menuId));
                    if (noneMatch) {
                        newRoleMenuIds.add(menuId);
                    }
                });
                //新增，调用批量插入方法
                if (!newRoleMenuIds.isEmpty()) {
                    RoleMenuBatchDO batchDO = new RoleMenuBatchDO();
                    batchDO.setRoleMenuIds(newRoleMenuIds);
                    batchDO.setRoleId(String.valueOf(record.getId()));
                    batchDO.setCreateTime(new Date());
                    batchDO.setUpdateTime(new Date());
                    count += roleMenuDao.insertBatch(batchDO);
                }
                //找出原来删除过的，这次又需要添加的
                List<String> updateRoleMenuIds = roleMenus.stream()
                        .filter(roleMenu -> Constants.CONSTANTS_Y.equals(roleMenu.getIsDeleted())
                                && roleMenuIds.contains(roleMenu.getMenuId()))
                        .map(RoleMenu::getMenuId).collect(Collectors.toList());
                if (null != updateRoleMenuIds && !updateRoleMenuIds.isEmpty()) {
                    RoleDTO updateRoleMenu = new RoleDTO();
                    updateRoleMenu.setId(roleDTO.getId());
                    updateRoleMenu.setIsDeleted(Constants.CONSTANTS_N);
                    updateRoleMenu.setRoleMenuIds(updateRoleMenuIds);
                    count += updateRoleMenusIsDeletedByRoleDTO(updateRoleMenu).getValue();
                }
            } else {
                //所有的都不选，删除该用户所有的角色菜单
                if (null != roleMenus && !roleMenus.isEmpty()) {
                    RoleMenu roleMenuRecord = new RoleMenu();
                    roleMenuRecord.setIsDeleted(Constants.CONSTANTS_Y);
                    roleMenuRecord.setUpdateTime(new Date());
                    count += roleMenuDao.updateByExampleSelective(roleMenuRecord, example);
                }
            }
            if (count > 0) {
                result.setValue(count);
            }
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("updateRoleByRoleDTO Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("updateRoleByRoleDTO end, {} row(s) affected", result.getValue());
        return result;
    }

    @Override
    public ResultBase<Integer> saveRoleByRoleDTO(RoleDTO roleDTO) throws BizException {
        log.info("saveRoleByRoleDTO start.menuDTO:{}",
                ToStringBuilder.reflectionToString(roleDTO, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        try {
            Role record = BeanConvertionHelp.copyBeanFieldValue(Role.class, roleDTO);
            record.setCreateTime(new Date());
            record.setUpdateTime(new Date());
            record.setIsDeleted(Constants.CONSTANTS_N);
            record.setModifier(Constants.SYSTEM);
            record.setCreator(Constants.SYSTEM);
            int count = roleDao.insertSelective(record);
            //保存角色菜单
            if (StringUtils.isNotBlank(roleDTO.getMenuIds())) {
                String[] ids = new String[0];
                if (StringUtils.isNotBlank(roleDTO.getMenuIds())) {
                    ids = roleDTO.getMenuIds().split(",");
                }
                List<String> roleMenuIds = Arrays.asList(ids);
                RoleMenuBatchDO batchDO = new RoleMenuBatchDO();
                batchDO.setRoleMenuIds(roleMenuIds);
                batchDO.setRoleId(String.valueOf(record.getId()));
                batchDO.setCreateTime(new Date());
                batchDO.setUpdateTime(new Date());
                count += roleMenuDao.insertBatch(batchDO);
            }
            log.info("saveRoleByRoleDTO {} rows affects", count);
            result.setValue(count);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("saveRoleByRoleDTO Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("saveRoleByRoleDTO end, {} row(s) affected", result.getValue());
        return result;
    }

    @Override
    public ResultBase<Integer> deleteRoleById(Long id) throws BizException {
        log.info("deleteRoleById start. id:{}", id);
        ResultBase<Integer> result = new ResultBase<>();
        if (id == null) {
            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL.getMsg(), id + "为空");
        }
        try {
            Role record = new Role();
            record.setId(id);
            record.setIsDeleted(Constants.CONSTANTS_Y);
            record.setUpdateTime(new Date());
            int count = roleDao.updateByPrimaryKeySelective(record);
            result.setValue(count);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("deleteRoleById Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("deleteRoleById end.");
        return result;
    }

    @Override
    public ResultBase<Role> selectRoleMenusByRoleId(String roleId) throws BizException {
        log.info("selectRoleMenusByRoleId start roleId:{}", roleId);
        ResultBase<Role> result = new ResultBase<>();
        try {
            Role role = roleDao.selectRoleMenusByRoleId(roleId);
            result.setSuccess(true);
            result.setValue(role);
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("selectRoleMenusByRoleId error:{}", e.getMessage(), e);
        }
        log.info("selectRoleMenusByRoleId end.");
        return result;
    }

    @Override
    public ResultBase<Integer> updateRoleMenusIsDeletedByRoleDTO(RoleDTO roleDTO) throws BizException {
        log.info("deleteRoleMenuByRoleDTO start.roleDTO:{}",
                ToStringBuilder.reflectionToString(roleDTO, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        try {
            int count = 0;
            RoleMenuQuery example = new RoleMenuQuery();
            example.createCriteria().andRoleIdEqualTo(String.valueOf(roleDTO.getId()))
                    .andMenuIdIn(roleDTO.getRoleMenuIds());
            RoleMenu record = new RoleMenu();
            record.setIsDeleted(roleDTO.getIsDeleted());
            record.setUpdateTime(new Date());
            count += roleMenuDao.updateByExampleSelective(record, example);
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
