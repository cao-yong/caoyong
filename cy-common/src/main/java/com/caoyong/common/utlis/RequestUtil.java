package com.caoyong.common.utlis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.caoyong.common.web.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * 获取 CSESSIONID
 * @author yong.cao
 * @time 2017年7月16日下午2:59:02
 */
@Slf4j
public class RequestUtil {
	/**
	 * 获取CSESSIONID
	 * @param request
	 * @return
	 */
	public static String getCSESSIONID(HttpServletRequest request, HttpServletResponse response){
		log.info("getCSESSIONID start.");
		//当CSESSIONID不存在时创建
		String csessionid = null;
		try {
			Cookie[] cookies = request.getCookies();
			if(null != cookies && cookies.length>0){
				for(Cookie cookie : cookies){
					if(Constants.CSESSIONID.equals(cookie.getName())){
						csessionid = cookie.getValue();
						return csessionid;
					}
				}
			}
			csessionid = RandomUUIDUtil.getRadomUUID();
			Cookie cookie = new Cookie(Constants.CSESSIONID, csessionid);
			
			//存活时间,关闭浏览器失效
			cookie.setMaxAge(-1);
			//路径,所有路径都带cookie
			cookie.setPath("/");
			//跨域
			//cookie.setDomain(".comfortabo.com");
			
			//cookie写回浏览器
			response.addCookie(cookie);
		} catch (Exception e) {
			log.error("getCSESSIONID error:", e.getMessage(), e);
		}
		log.info("getCSESSIONID end.");
		return csessionid;
	}
}
