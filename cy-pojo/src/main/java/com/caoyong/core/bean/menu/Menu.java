package com.caoyong.core.bean.menu;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Menu implements Serializable {
    private static final long serialVersionUID = -4207555377629634936L;

    /**
     * 主键
     */
    private String            id;

    /**
     * 父级菜单
     */
    private Menu              parent;

    /**
     * 父ID
     */
    private String            parentId;

    /**
     * 名称
     */
    private String            name;

    /**
     * 排序
     */
    private Integer           sort;

    /**
     * 链接
     */
    private String            href;

    /**
     * 图标
     */
    private String            icon;

    /**
     * 是否展示（0：不展示，1：展示）
     */
    private Integer           isShow;

    /**
     * 权限
     */
    private String            permission;

    /**
     * 备注
     */
    private String            remarks;

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
     * 是否删除（Y:是，N:否）
     */
    private String            isDeleted;
}
