package com.caoyong.core.bean.statistics;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VisitsQueryDTO implements Serializable {

    private static final long serialVersionUID = -7804004908705816877L;

    /**
     * 主键
     */
    private Long              id;

    /**
     * 会话ID
     */
    private String            sessionId;

    /**
     * 访问IP
     */
    private String            ip;

    /**
     * 访问页面
     */
    private String            page;

    private Date              startDate;

    private Date              endDate;

}
