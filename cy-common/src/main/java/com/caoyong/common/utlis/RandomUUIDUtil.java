package com.caoyong.common.utlis;

import java.util.UUID;

/**
 * 随机获取uuid(不带'-')
 * 
 * @author yong.cao
 * @time 2017年7月16日下午3:18:08
 */
public class RandomUUIDUtil {

    /**
     * 获取uuid
     * 
     * @return
     */
    public static String getRadomUUID() {
        String value = UUID.randomUUID().toString().replaceAll("-", "");
        return value;
    }
}
