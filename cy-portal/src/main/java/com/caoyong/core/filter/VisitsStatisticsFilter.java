package com.caoyong.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.statistics.Visits;
import com.caoyong.core.bean.statistics.VisitsDTO;
import com.caoyong.core.bean.statistics.VisitsQueryDTO;
import com.caoyong.core.service.statistics.VisitsService;

import lombok.extern.slf4j.Slf4j;

/**
 * 访客统计filter
 * 
 * @author caoyong
 * @time 2018年1月10日 下午1:52:16
 */
@Slf4j
public class VisitsStatisticsFilter implements Filter {

    @Reference(version = "1.0.0")
    private VisitsService visitsService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("VisitsStatisticsFilter init");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("OncePerRequestFilter just supports HTTP requests");
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        String ip = httpRequest.getRemoteAddr();
        String page = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String servletPath = httpRequest.getServletPath();

        log.info("doFilter sessionId=" + session.getId() + ",ip=" + ip + ",page=" + page + ",contextPath=" + contextPath
                + ",servletPath=" + servletPath);

        VisitsDTO visits = new VisitsDTO();
        visits.setSessionId(session.getId());
        visits.setIp(ip);
        visits.setPage(page);
        visits.setDuration(0L);

        //通过session id 和 ip，查出最近的一条访问记录
        Visits bean = null;
        try {
            VisitsQueryDTO query = new VisitsQueryDTO();
            query.setSessionId(session.getId());
            query.setIp(ip);
            ResultBase<Visits> visitisBase = visitsService.queryLatestRecord(query);
            if (visitisBase.isSuccess()) {
                bean = visitisBase.getValue();
            }
        } catch (Exception e) {
            log.error("queryLatestRecord in filter error:{}", e.getMessage(), e);
        }

        //更改最近访问记录的停留时间，这里把两次访问记录的间隔时间算成上一次页面访问的停留时间
        if (bean != null) {
            long stayTime = (System.currentTimeMillis() - bean.getCreateTime().getTime()) / 1000;
            try {
                VisitsDTO visitsUpdate = new VisitsDTO();
                visitsUpdate.setId(bean.getId());
                visitsUpdate.setDuration(stayTime);
                visitsService.updateVisits(visitsUpdate);
            } catch (Exception e) {
                log.error("updateVisits in filter error:{}", e.getMessage(), e);
            }
        }

        //保存当前访问记录
        try {
            //对静态资源不做处理
            if (!page.contains("static")) {
                visitsService.saveVisits(visits);
            }
        } catch (Exception e) {
            log.error("saveVisits in filter error:{}", e.getMessage(), e);
        }
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        log.info("VisitsStatisticsFilter destroy");
    }

}
