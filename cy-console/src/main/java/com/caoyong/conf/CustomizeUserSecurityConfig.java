package com.caoyong.conf;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.system.Role;
import com.caoyong.core.bean.system.User;
import com.caoyong.core.service.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录验证逻辑
 *
 * @author yong.cao
 * @since 2017年7月15日下午10:35:02
 */
@Slf4j
@Component
public class CustomizeUserSecurityConfig implements UserDetailsService {
    @Reference(version = "1.0.0", timeout = 3000000)
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        try {
            ResultBase<User> resultBase = userService.queryUserAndRolesByUsername(username);
            if (resultBase.isSuccess() && resultBase.getValue() != null) {
                user = resultBase.getValue();
            }
            if (user != null) {
                //用于添加用户的权限。把用户权限添加到authorities
                for (Role role : user.getRoles()) {
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                }
                log.info("user:{} login success", user.getUsername());
            } else {
                log.info("user:{} not exist", username);
                return new org.springframework.security.core.userdetails.User("", "", authorities);
            }
        } catch (Exception e) {
            log.error("loadUserByUsername error:{}", e.getMessage(), e);
            return new org.springframework.security.core.userdetails.User("", "", authorities);
        }
        return new MyUserDetail(user.getUsername(), user.getPassword(), authorities, user.getId(), user.getRoles(), user.getName());
    }

}
