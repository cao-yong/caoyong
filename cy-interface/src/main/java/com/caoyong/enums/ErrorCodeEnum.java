package com.caoyong.enums;

public enum ErrorCodeEnum {
	
	UNKOWN_ERROR("9999","未知错误"),
	DATA_BASE_ACCESS_ERROR("1001","访问数据库错误"),
	PARAMETER_CAN_NOT_BE_NULL("1002","参数不能为空"),
	NUM_FORMATE_ERROR("1003","数字格式化错误"),
	SOLR_SERVER_ERROR("1004","solr服务器错误"),
	IO_ERROR("1005","IO错误");
	private String code;
	private String msg;
	private ErrorCodeEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	/**
	 *获得枚举 
	 * @param code
	 * @return
	 */
	public static ErrorCodeEnum getEnum(String code){
		if(null!=code){
			for(ErrorCodeEnum en : ErrorCodeEnum.values()){
				if(en.getCode().equals(code)){
					return en;
				}
			}
		}
		return null;
	}
}
