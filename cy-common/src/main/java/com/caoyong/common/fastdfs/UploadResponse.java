package com.caoyong.common.fastdfs;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * 上传文件返回内容
 * 
 * @author caoyong
 * @time 2017年11月28日 下午2:38:14
 */
public class UploadResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 图片url，适用于单张时
     */
    private String            url;
    /**
     * 是否成功
     */
    private Boolean           isSuccess        = Boolean.FALSE;

    /**
     * 错误消息
     */
    private String            errorMsg;

    /**
     * 是否错误，1是，0否
     */
    private Integer           error            = 0;
    /**
     * 图片url，适用于多张时
     */
    private List<String>      urls;
}
