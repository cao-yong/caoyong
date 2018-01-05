package com.caoyong.core.bean.statistics;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VisitsDTO implements Serializable {

    private static final long serialVersionUID = -2154134392124166168L;

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

    /**
     * 停留时间(毫秒)
     */
    private Long              duration;

}
