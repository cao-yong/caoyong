package com.caoyong.core.bean.base;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 返回基类
 * 
 * @author caoyong
 * @time 2017年10月30日 下午6:00:10
 */
@Getter
@Setter
@ToString
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 3230910710976511975L;
    /**
     * 定义的code
     */
    private String            code;
    /**
     * 消息
     */
    private String            msg;

    /**
     * 是否成功
     */
    private boolean           isSuccess;

}
