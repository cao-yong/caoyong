package com.caoyong.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.utlis.RequestUtil;
import com.caoyong.core.service.user.SessionProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义拦截器
 * 
 * @author yong.cao
 * @time 2017年7月23日下午1:53:21
 */

@Slf4j
public class CustomInterceptor implements HandlerInterceptor {
    @Reference(version = "1.0.0")
    private SessionProvider sessionProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("CustomInterceptor preHandle start.");
        //用户必须登录 
        String uername = sessionProvider.getAttributeForUser(RequestUtil.getCSESSIONID(request, response));
        //未登录
        if (StringUtils.isBlank(uername)) {
            response.sendRedirect("//localhost:8081/login.aspx?returnUrl=//localhost:8082/");
            return false;
        }
        log.info("CustomInterceptor preHandle end.");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView)
            throws Exception {
        log.info("CustomInterceptor postHandle.");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        log.info("CustomInterceptor afterCompletion.");
    }

}
