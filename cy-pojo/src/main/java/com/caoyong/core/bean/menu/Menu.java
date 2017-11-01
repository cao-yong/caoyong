package com.caoyong.core.bean.menu;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Menu implements Serializable {
    private static final long serialVersionUID = -4207555377629634936L;

    /**
     * 主键
     */
    @Getter
    @Setter
    private String            id;

    /**
     * 父级菜单
     */
    @Getter
    @Setter
    private Menu              parent;

    /**
     * 父ID
     */
    private String            parentId;

    /**
     * 名称
     */
    @Getter
    @Setter
    private String            name;

    /**
     * 排序
     */
    @Getter
    @Setter
    private Integer           sort;

    /**
     * 链接
     */
    @Getter
    @Setter
    private String            href;

    /**
     * 图标
     */
    @Getter
    @Setter
    private String            icon;

    /**
     * 是否展示（0：不展示，1：展示）
     */
    @Getter
    @Setter
    private Integer           isShow;

    /**
     * 权限
     */
    @Getter
    @Setter
    private String            permission;

    /**
     * 备注
     */
    @Getter
    @Setter
    private String            remarks;

    /**
     * 创建时间
     */
    @Getter
    @Setter
    private Date              createTime;

    /**
     * 修改时间
     */
    @Getter
    @Setter
    private Date              updateTime;

    /**
     * 创建者
     */
    @Getter
    @Setter
    private String            creator;

    /**
     * 修改者
     */
    @Getter
    @Setter
    private String            modifier;

    /**
     * 扩展字段
     */
    @Getter
    @Setter
    private String            extraInfo;

    /**
     * 是否删除（Y:是，N:否）
     */
    @Getter
    @Setter
    private String            isDeleted;

    public String getParentId() {
        parentId = parent != null && parent.getId() != null ? parent.getId() : "0";
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
