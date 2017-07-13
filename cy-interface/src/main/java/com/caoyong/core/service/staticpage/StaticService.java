package com.caoyong.core.service.staticpage;

import java.util.Map;

public interface StaticService {
	/**
	 * 商品静态页面
	 * @param root
	 * @param id
	 */
	void productStaticPage(Map<String, Object> root, String id);
	/**
	 * 获取全路径
	 * @param name
	 * @return
	 */
	String getPath(String name);

}
