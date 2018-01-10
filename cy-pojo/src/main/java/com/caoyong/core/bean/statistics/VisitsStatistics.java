package com.caoyong.core.bean.statistics;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 查询访问量统计数据
 * 
 * @author caoyong
 * @time 2018年1月10日 上午10:34:13
 */
@Getter
@Setter
@ToString
public class VisitsStatistics implements Serializable {

    private static final long serialVersionUID = 1743150455513907758L;
    /**
     * 点击量
     */
    private Integer           pv;
    /**
     * 独立访客
     */
    private Integer           uv;
    /**
     * ip统计
     */
    private Integer           ip;
    /**
     * 时间周期
     */
    private String            time;
    /**
     * 时间周期名称
     */
    private String            timeName;
}
