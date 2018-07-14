package com.caoyong.enums;

/**
 * 错误枚举
 * 
 * @author yong.cao
 * @time 2017年7月30日 下午9:35:34
 */
public enum ErrorCodeEnum {
    UNKOWN_ERROR("9999", "未知错误"),
    DATA_BASE_ACCESS_ERROR("1001", "访问数据库错误"),
    PARAMETER_CAN_NOT_BE_NULL("1002", "参数不能为空"),
    NUM_FORMATE_ERROR("1003", "数字格式化错误"),
    SOLR_SERVER_ERROR("1004", "solr服务器错误"),
    IO_ERROR("1005", "IO错误"),
    PROCESS_DATA_ERROR("1006", "数据处理错误"),
    LOGIN_INFO_NULL("1007", "请输入账户名和密码"),
    USERNAME_CAN_NOT_BE_NULL("1008", "请输入账户名"),
    PASSWORD_CAN_NOT_BE_NULL("1009", "请输入密码"),
    PASSWORD_ERROR("1010", "账户名与密码不匹配，请重新输入"),
    USER_NOT_EXIST("1011", "账户名不存在，请重新输入"),
    USER_EXIST("1012", "登录名已存在，请重新输入"),
    CAN_NOT_CONTAINS_SELF_LIST("1013", "类中包含自身集合,不能使用图片处理工具类");

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
     * 获得枚举
     * 
     * @param code
     * @return
     */
    public static ErrorCodeEnum getEnum(String code) {
        if (null != code) {
            for (ErrorCodeEnum en : ErrorCodeEnum.values()) {
                if (en.getCode().equals(code)) {
                    return en;
                }
            }
        }
        return null;
    }
}
