package com.caoyong.core.service.statistics;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.caoyong.common.enums.VisitsStatisticsTimeEnum;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.statistics.Visits;
import com.caoyong.core.bean.statistics.VisitsDTO;
import com.caoyong.core.bean.statistics.VisitsQuery;
import com.caoyong.core.bean.statistics.VisitsQueryDTO;
import com.caoyong.core.bean.statistics.VisitsStatistics;
import com.caoyong.core.dao.statistics.VisitsDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;
import com.caoyong.utils.BeanConvertionHelp;
import com.caoyong.utils.CheckParamsUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 访问用户Service实现
 * 
 * @author caoyong
 * @time 2018年1月10日 上午10:45:12
 */
@Slf4j
@Service(version = "1.0.0")
public class VisitsServiceImpl implements VisitsService {
    @Autowired
    private VisitsDao visitsDao;

    @Override
    public ResultBase<Visits> queryLatestRecord(VisitsQueryDTO query) throws BizException {
        log.info("queryLatestRecord start. query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Visits> result = new ResultBase<>();
        try {
            VisitsQuery example = new VisitsQuery();
            example.createCriteria().andSessionIdEqualTo(query.getSessionId()).andIpEqualTo(query.getIp());
            example.setPageSize(1);
            example.setPageNo(1);
            example.setOrderByClause("create_time desc");
            List<Visits> visits = visitsDao.selectByExample(example);
            if (null != visits && !visits.isEmpty()) {
                result.setSuccess(true);
                result.setValue(visits.get(0));
            }
        } catch (Exception e) {
            result.setErrorCode(e.getMessage());
            result.setErrorMsg("查询上一条访问记录失败");
            log.error("selectColorList error:{}", e.getMessage(), e);
        }
        log.info("queryLatestRecord end.");
        return result;
    }

    @Override
    public ResultBase<Integer> updateVisits(VisitsDTO visitsDTO) throws BizException {
        log.info("updateVisits start, visitsDTO:{}",
                ToStringBuilder.reflectionToString(visitsDTO, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        try {
            CheckParamsUtil.check(visitsDTO, VisitsDTO.class, "id");
            Visits visitis = BeanConvertionHelp.copyBeanFieldValue(Visits.class, visitsDTO);
            visitis.setUpdateTime(new Date());
            int count = visitsDao.updateByPrimaryKeySelective(visitis);
            if (count > 0) {
                result.setSuccess(true);
                result.setValue(count);
            }
            log.info("updateVisits result count:{}", count);
        } catch (Exception e) {
            log.error("updateVisits Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("updateVisits end.");
        return result;
    }

    @Override
    public ResultBase<List<VisitsStatistics>> selectVisitsStatistics() throws BizException {
        log.info("selectVisitsStatistics start.");
        ResultBase<List<VisitsStatistics>> result = new ResultBase<>();
        try {
            List<VisitsStatistics> visitsStatistics = visitsDao.selectVisitsStatistics();
            if (null != visitsStatistics) {
                visitsStatistics.stream().forEach(visitsSta -> visitsSta
                        .setTimeName(VisitsStatisticsTimeEnum.getNameByCode(visitsSta.getTime())));
                result.setValue(visitsStatistics);
                result.setSuccess(true);
            }
        } catch (Exception e) {
            result.setErrorCode(e.getMessage());
            result.setErrorMsg("查询访客统计数据失败");
            log.error("selectVisitsStatistics error:{}", e.getMessage(), e);
        }
        log.info("selectVisitsStatistics end.");
        return result;
    }

    @Override
    public ResultBase<Integer> saveVisits(VisitsDTO visitsDTO) throws BizException {
        log.info("saveVisits start, visitsDTO:{}",
                ToStringBuilder.reflectionToString(visitsDTO, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        try {
            Visits record = BeanConvertionHelp.copyBeanFieldValue(Visits.class, visitsDTO);
            record.setCreateTime(new Date());
            record.setUpdateTime(new Date());
            int count = visitsDao.insertSelective(record);
            if (count > 0) {
                result.setSuccess(true);
                result.setValue(count);
            }
        } catch (Exception e) {
            log.error("saveVisits Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("saveVisits end.");
        return result;
    }

}
