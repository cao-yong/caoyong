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
public class Role implements Serializable {

    private static final long serialVersionUID = 1644155928561565023L;

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

    private List<Menu>        menuList;

}
