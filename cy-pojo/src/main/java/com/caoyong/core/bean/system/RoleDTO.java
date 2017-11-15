package com.caoyong.core.bean.system;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleDTO implements Serializable {
    private static final long serialVersionUID = -307694084028279835L;

    /**
     * 主键
     */
    private Long              id;

    /**
     * 名字
     */
    private String            name;

    /**
     * 英文名
     */
    private String            englishName;

    /**
     * 是否可用（0：不可用，1：可用）
     */
    private Integer           isEnable;

    /**
     * 备注
     */
    private String            remark;

    /**
     * 保存角色菜单id
     */
    private String            menuIds;

    private List<String>      roleMenuIds;

    /**
     * 是否删除（Y：是，N：否）
     */
    private String            isDeleted;
}
