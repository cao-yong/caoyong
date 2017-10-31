package com.caoyong.core.bean.menu;

import java.util.List;

import com.caoyong.core.bean.base.BaseResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChosenMenuIconsResp extends BaseResponse {

    private static final long serialVersionUID = 4294394330339152065L;

    private List<String>      icons;
}
