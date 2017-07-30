package com.caoyong.core.service.staticpage;

import java.util.Map;

/**
 * 静态化
 * 
 * @author yong.cao
 * @time 2017年7月30日 下午9:34:05
 */
public interface StaticPageService {
    /**
     * 商品静态页面
     * 
     * @param root
     * @param id
     */
    void productStaticPage(Map<String, Object> root, String id);

    /**
     * 获取全路径
     * 
     * @param name
     * @return
     */
    String getPath(String name);

}
