package com.caoyong.conf;

import com.caoyong.core.bean.system.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * 用户详情
 *
 * @author yong.cao
 * @since 2017年7月15日下午10:35:02
 */
public class MyUserDetail extends User {
    private Integer userId;
    private String charactorName;
    private List<Role> roleList;

    public MyUserDetail(String username, String password, boolean enabled, boolean accountNonExpired,
                        boolean credentialsNonExpired, boolean accountNonLocked,
                        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public MyUserDetail(Integer userId, String username, String password,
                        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public MyUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities,
                        Integer userId, List<Role> roleList, String charactorName) {
        super(username, password, authorities);
        this.userId = userId;
        this.roleList = roleList;
        this.charactorName = charactorName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getCharactorName() {
        return charactorName;
    }

    public void setCharactorName(String charactorName) {
        this.charactorName = charactorName;
    }


}
