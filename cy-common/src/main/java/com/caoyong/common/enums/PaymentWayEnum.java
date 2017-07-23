package com.caoyong.common.enums;

import java.util.Optional;
import java.util.stream.Stream;
/**
 * 支付方式枚举
 * @author yong.cao
 * @time 2017年7月23日下午6:32:36
 */
public enum PaymentWayEnum {
	CASH_ON_DELIVERY(0, "到付"),
	ONLINE(1, "在线"),
	POST(2, "邮局"),
	COMPANY_TRANSFER(3, "公司转帐");
	
	private Integer code;
	private String name;
	
	private PaymentWayEnum(Integer code, String name){
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
	public static PaymentWayEnum getEnum(Integer code){
		 Optional<PaymentWayEnum> en = Stream
		.of(PaymentWayEnum.values())
		.filter(e -> e.code.equals(code))
		.findFirst();
		 if(en.isPresent()){
			 return en.get();
		 }
		 return null;
	}
}
