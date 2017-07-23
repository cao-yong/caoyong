package com.caoyong.common.enums;

import java.util.Optional;
import java.util.stream.Stream;
/**
 * 货到付款方式枚举
 * @author yong.cao
 * @time 2017年7月23日下午6:32:57
 */
public enum PaymentCashEnum {
	CASH(1, "现金"),
	POS(2, "POS刷卡");
	
	private Integer code;
	private String name;
	
	private PaymentCashEnum(Integer code, String name){
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
	public static PaymentCashEnum getEnum(Integer code){
		 Optional<PaymentCashEnum> en = Stream
		.of(PaymentCashEnum.values())
		.filter(e -> e.code.equals(code))
		.findFirst();
		 if(en.isPresent()){
			 return en.get();
		 }
		 return null;
	}
}
