package com.caoyong.core.bean.product;

import java.io.Serializable;

import com.caoyong.core.bean.base.BaseQuery;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SkuQueryDTO extends BaseQuery implements Serializable {
    private static final long serialVersionUID = 1499746010635737905L;

    private Long              productId;
}
