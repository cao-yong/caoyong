package com.caoyong.conf;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.caoyong.core.filter.VisitsStatisticsFilter;
import com.caoyong.core.interceptor.CustomInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    /**
     * 先加载拦截器，否则dubbo服务为空
     * 
     * @return 拦截器
     */
    @Bean
    public CustomInterceptor customInterceptor() {
        return new CustomInterceptor();
    }

    /**
     * 先加载过滤器，否则dubbo服务为空
     * 
     * @return 过滤器
     */
    @Bean
    public VisitsStatisticsFilter visitsStatisticsFilter() {
        return new VisitsStatisticsFilter();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customInterceptor()).addPathPatterns("/buyer/**");
        super.addInterceptors(registry);
    }

    /**
     * 配置过滤器
     * 
     * @return
     */
    @Bean
    public FilterRegistrationBean indexFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(visitsStatisticsFilter());
        registration.addUrlPatterns("/");
        return registration;
    }

}
