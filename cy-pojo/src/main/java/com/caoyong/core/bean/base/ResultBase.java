package com.caoyong.core.bean.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.ToString;
/**
 * 返回结果对象
 * @author yong.cao
 * @time 2017年6月21日下午9:31:48
 */
@ToString
public class ResultBase<T> implements Serializable {

	private static final long serialVersionUID = 5060957039291207838L;
	private boolean             isSuccess        = false;
    private String              errorMsg         = "";
    private String              errorCode        = "";
    private T                   value;
    private Map<String, Object> additionalInfo   = new HashMap<String, Object>();

    public ResultBase() {
        super();
    }

    public ResultBase(T value) {
        super();
        this.isSuccess = true;
        this.value = value;
    }

    public ResultBase(String errorMsg, String errorCode) {
        super();
        this.isSuccess = false;
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public ResultBase(boolean success, String errorMsg, String errorCode) {
        super();
        this.isSuccess = success;
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void appendAdditionalInfo(String key, Object value) {
        this.additionalInfo.put(key, value);
    }

    @SuppressWarnings("unchecked")
    //
    public <P> P getAdditionalInfo(String key) {
        return (P) additionalInfo.get(key);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
}
