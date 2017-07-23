package com.caoyong.common.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 支付状态枚举
 * @author yong.cao
 * @time 2017年7月23日下午6:34:13
 */
public enum PaymentStateEnum {
	CASH_ON_DELIVERY(0, "到付"),
	PENDING_PAYMENT(1, "待付款"),
	PAID(2, "已付款"),
	TO_BE_REFUNDED(3, "待退款"),
	REFUND_SUCCESSFULLY(4, "退款成功"),
	REFUND_FAILED(5, "退款失败");
	private Integer code;
	private String name;
	
	private PaymentStateEnum(Integer code, String name){
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
	public static PaymentStateEnum getEnum(Integer code){
		 Optional<PaymentStateEnum> en = Stream
		.of(PaymentStateEnum.values())
		.filter(e -> e.code.equals(code))
		.findFirst();
		 if(en.isPresent()){
			 return en.get();
		 }
		 return null;
	}
}
