package com.caoyong.core.service.user;

/**
 * 用户session服务
 * 
 * @author yong.cao
 * @time 2017年7月16日下午1:36:22
 */

public interface SessionProvider {
    /**
     * 保存用户信息到redis
     * 
     * @param name
     * @param value
     */
    void setAttributeForUser(String name, String value);

    /**
     * 从redis获取用户名
     * 
     * @param name
     * @return
     */
    String getAttributeForUser(String name);
}
