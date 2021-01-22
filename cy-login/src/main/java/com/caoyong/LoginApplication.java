package com.caoyong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 入口
 *
 * @author caoyong 2017年10月24日 下午3:29:57
 */
@EnableScheduling
@SpringBootApplication
public class LoginApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LoginApplication.class);
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (container) -> {
            ErrorPage error403Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/static/403.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/static/404.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/static/500.html");
            container.addErrorPages(error403Page, error404Page, error500Page);
        };
    }

    /**
     * 相当于servlet-mapping,url-pattern
     *
     * @param dispatcherServlet 转发器
     * @return servletBean
     */
    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings("*.aspx");
        registration.addUrlMappings("*.json");
        registration.addUrlMappings("/static/*");
        return registration;
    }

}
