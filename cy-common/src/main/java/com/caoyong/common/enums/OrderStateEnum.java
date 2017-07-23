package com.caoyong.common.enums;

import java.util.Optional;
import java.util.stream.Stream;
/**
 * 订单状态枚举
 * @author yong.cao
 * @time 2017年7月23日下午6:33:28
 */
public enum OrderStateEnum {
	SUBMIT_ORDER(0, "提交订单"),
	DISTRIBUTION(1, "仓库配货"),
	OUT_OF_STOCK(2, "商品出库"),
	WAITING_FOR_DELIVERY(3, "等待收货"),
	COMPLETE(4, "完成"),
	WAITING_FOR_REFUNDING(5, "待退货"),
	ALREADY_REFUNDING(6, "已退货");
	 
	private Integer code;
	private String name;
	
	private OrderStateEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}
	public Integer getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	/**
	 * 获取枚举
	 * @param code
	 * @return
	 */
	public static OrderStateEnum getEnum(Integer code){
		 Optional<OrderStateEnum> en = Stream
		.of(OrderStateEnum.values())
		.filter(e -> e.code.equals(code))
		.findFirst();
		 if(en.isPresent()){
			 return en.get();
		 }
		 return null;
	}
}
