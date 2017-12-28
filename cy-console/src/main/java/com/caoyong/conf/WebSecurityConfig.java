package com.caoyong.conf;

import com.caoyong.common.utlis.EncodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security权限核心模块
 *
 * @author yong.cao
 * @since 2017年7月15日下午10:35:02
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomizeUserSecurityConfig customizeUserSecurityConfig;
    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    /**
     * 注册UserDetailsService 的bean
     *
     * @return
     */
    @Bean
    UserDetailsService customUserService() {
        System.out.println("注册自定义userService" + customizeUserSecurityConfig.toString());
        return customizeUserSecurityConfig;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("使用自定义customizeUserSecurityConfig" + customizeUserSecurityConfig);
        //user Details Service验证
        auth.userDetailsService(customizeUserSecurityConfig).passwordEncoder(new PasswordEncoder() {
            public String encode(CharSequence rawPassword) {
                return EncodeUtil.encodePassword((String) rawPassword);
            }

            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(EncodeUtil.encodePassword((String) rawPassword));
            }
        });

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        //关闭打开的csrf保护
        http.csrf().disable();
        http
                .headers().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/control/login.do").successHandler(customSuccessHandler)
                .permitAll()
                .and()
                .logout().logoutUrl("/control/logout.do").permitAll()
                .logoutSuccessUrl("/control/login.do");
        log.info("http规则配置");

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }
}

