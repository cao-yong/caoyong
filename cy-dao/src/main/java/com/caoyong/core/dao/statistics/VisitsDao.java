package com.caoyong.core.dao.statistics;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.caoyong.core.bean.statistics.Visits;
import com.caoyong.core.bean.statistics.VisitsQuery;
import com.caoyong.core.bean.statistics.VisitsStatistics;

public interface VisitsDao {
    int countByExample(VisitsQuery example);

    int deleteByExample(VisitsQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Visits record);

    int insertSelective(Visits record);

    List<Visits> selectByExample(VisitsQuery example);

    Visits selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Visits record, @Param("example") VisitsQuery example);

    int updateByExample(@Param("record") Visits record, @Param("example") VisitsQuery example);

    int updateByPrimaryKeySelective(Visits record);

    int updateByPrimaryKey(Visits record);

    List<VisitsStatistics> selectVisitsStatistics();
}
