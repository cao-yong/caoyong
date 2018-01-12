package com.caoyong.core.service.statistics;

import java.util.List;

import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.statistics.Visits;
import com.caoyong.core.bean.statistics.VisitsDTO;
import com.caoyong.core.bean.statistics.VisitsQueryDTO;
import com.caoyong.core.bean.statistics.VisitsStatistics;
import com.caoyong.exception.BizException;

/**
 * 用户访问量Service
 * 
 * @author caoyong
 * @time 2018年1月5日 下午6:03:07
 */
public interface VisitsService {
    /**
     * 查询最近一次访问的记录
     * 
     * @param query 查询条件
     * @return 访问记录
     */
    ResultBase<Visits> queryLatestRecord(VisitsQueryDTO query) throws BizException;

    /**
     * 保存用户访问
     * 
     * @param visitsDTO 数据对象
     * @return 影响的行数
     */
    ResultBase<Integer> saveVisits(VisitsDTO visitsDTO) throws BizException;

    /**
     * 更新用户访问
     * 
     * @param visitsDTO 数据对象
     * @return 影响的行数
     */
    ResultBase<Integer> updateVisits(VisitsDTO visitsDTO) throws BizException;

    /**
     * 查询访问量统计数据
     * 
     * @return 结果
     */
    ResultBase<List<VisitsStatistics>> selectVisitsStatistics() throws BizException;
}
