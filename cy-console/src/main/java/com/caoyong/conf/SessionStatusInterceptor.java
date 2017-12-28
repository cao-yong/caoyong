package com.caoyong.conf;

import com.alibaba.fastjson.JSONObject;
import com.caoyong.core.bean.system.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Security会话状态拦截
 *
 * @author yong.cao
 * @since 2017年7月15日下午10:35:02
 */
public class SessionStatusInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        String requestType = request.getHeader("X-Requested-With");
        JSONObject result = new JSONObject();
        if (currentUser == null && StringUtils.isNotBlank(requestType) && requestType.equalsIgnoreCase("XMLHttpRequest")) {
            result.put("sessionStatus", 540);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(result.toString());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
