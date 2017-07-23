package com.caoyong.core.bean.cart;

import java.io.Serializable;

import com.caoyong.core.bean.product.Sku;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 购物项
 * @author yong.cao
 * @time 2017年7月17日下午10:37:24
 */
@Getter
@Setter
@ToString
public class BuyerItem implements Serializable{
	
	private static final long serialVersionUID = 7844850993903682488L;

	/**
	 * sku对象
	 */
	private Sku sku;
	
	/**
	 * 是否有货
	 */
	private Boolean isHave = true;
	
	/**
	 * 数量
	 */
	private Integer amount = 1;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuyerItem other = (BuyerItem) obj;
		if (sku == null) {
			if (other.sku != null)
				return false;
			//重写equals方法
		} else if (!sku.getId().equals(other.sku.getId()))
			return false;
		return true;
	}
	
	
}
