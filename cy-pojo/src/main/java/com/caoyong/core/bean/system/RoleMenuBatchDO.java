package com.caoyong.core.bean.system;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleMenuBatchDO implements Serializable {

    private static final long serialVersionUID = 2230665831782501527L;

    private String            roleId;

    private List<String>      roleMenuIds;
    /**
     * 创建时间
     */
    private Date              createTime;

    /**
     * 修改时间
     */
    private Date              updateTime;

}
