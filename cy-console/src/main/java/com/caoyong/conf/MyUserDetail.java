package com.caoyong.conf;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.caoyong.core.bean.system.Role;

/**
 * 用户详情
 *
 * @author yong.cao
 * @since 2017年7月15日下午10:35:02
 */
public class MyUserDetail extends User {
    private static final long serialVersionUID = -5601031290898392331L;
    /**
     * 用户id
     */
    private Integer           userId;
    /**
     * 用户名字
     */
    private String            name;
    /**
     * 角色列表
     */
    private List<Role>        roleList;

    public MyUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public MyUserDetail(String username, String password, boolean enabled, boolean accountNonExpired,
                        boolean credentialsNonExpired, boolean accountNonLocked,
                        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    MyUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities, Integer userId,
                 List<Role> roleList, String name) {
        super(username, password, authorities);
        this.userId = userId;
        this.roleList = roleList;
        this.name = name;
    }

    Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
