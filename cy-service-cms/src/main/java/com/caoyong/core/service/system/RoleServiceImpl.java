package com.caoyong.core.service.system;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.BaseQuery;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.MenuDTO;
import com.caoyong.core.bean.system.Role;
import com.caoyong.core.bean.system.RoleDTO;
import com.caoyong.core.bean.system.RoleQuery;
import com.caoyong.core.dao.system.RoleDao;
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
@Service("roleService")
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public ResultBase<List<Role>> queryRoleList(BaseQuery query) throws BizException {
        log.info("queryRoleList start. query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        ResultBase<List<Role>> result = new ResultBase<>();
        try {
            RoleQuery example = new RoleQuery();
            example.createCriteria().andIsDeletedEqualTo(Constants.CONSTANTS_N);
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
            example.setStartRow(query.getStart());
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
            CheckParamsUtil.check(roleDTO, MenuDTO.class, "id");
            Role record = BeanConvertionHelp.copyBeanFieldValue(Role.class, roleDTO);
            record.setUpdateTime(new Date());

            int count = roleDao.updateByPrimaryKeySelective(record);
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
        log.info("updateRoleByRoleDTO end.");
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
        } catch (Exception e) {
            log.error("saveRoleByRoleDTO Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("saveRoleByRoleDTO end.");
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
            if (count > 0) {
                result.setValue(count);
            }
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

}
