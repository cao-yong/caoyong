package com.caoyong.core.bean.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = -7173493818216806624L;

    /**
     * 主键
     */
    private Integer           id;

    /**
     * 用户名
     */
    private String            username;

    /**
     * 密码
     */
    private String            password;

    /**
     * 中文名
     */
    private String            name;

    /**
     * 邮箱地址
     */
    private String            email;

    /**
     * 手机号码
     */
    private String            phone;

    /**
     * 是否可用
     */
    private Integer           isEnable         = 1;

    /**
     * 最后登录IP
     */
    private String            loginIp;

    /**
     * 最后登陆时间
     */
    private Date              operateDate;

    /**
     * 创建时间
     */
    private Date              createTime;

    /**
     * 修改时间
     */
    private Date              updateTime;

    /**
     * 创建者
     */
    private String            creator;

    /**
     * 修改者
     */
    private String            modifier;

    /**
     * 扩展字段
     */
    private String            extraInfo;

    /**
     * 是否删除（Y：是，N：否）
     */
    private String            isDeleted;

    private List<Role>        roles            = new ArrayList<>();

    @JsonIgnore
    public List<Long> getRoleIdList() {
        List<Long> roleIdList = new ArrayList<>();
        for (Role role : roles) {
            roleIdList.add(role.getId());
        }
        return roleIdList;
    }

    @JsonIgnore
    public void setRoleIdList(List<Long> roleIdList) {
        roles = new ArrayList<>();
        for (Long roleId : roleIdList) {
            Role role = new Role();
            role.setId(roleId);
            roles.add(role);
        }
    }
    public boolean isAdmin(){
        return this.id.equals(1);
    }
}
