package com.caoyong.core.bean.system;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MenuTreeNodeVO implements Serializable {
    private static final long serialVersionUID = -1716940445035278294L;

    private String            code;
    private Menu              msg;
    private List<String>      icons;
}
