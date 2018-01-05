package com.caoyong.core.service.statistics;

import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.statistics.Visits;
import com.caoyong.core.bean.statistics.VisitsDTO;
import com.caoyong.core.bean.statistics.VisitsQueryDTO;

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
    ResultBase<Visits> queryLatestRecord(VisitsQueryDTO query);

    /**
     * 更新用户访问
     * 
     * @param visitsDTO 数据对象
     * @return 影响的行数
     */
    ResultBase<Integer> updateVisits(VisitsDTO visitsDTO);
}
