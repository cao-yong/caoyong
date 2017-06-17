package com.caoyong.common.fastdfs;

import java.io.Serializable;

import lombok.Data;
/**
 * 上传文件vo
 * @author yong.cao
 * @time 2017年6月14日下午10:56:53
 */
public @Data class UploadFileVo implements Serializable{
	
	private static final long serialVersionUID = -1829317741938100757L;
	/**
	 * 文件二进制
	 */
	private byte[] pic;
	/**
	 * 文件名称
	 */
	private String name;
	/**
	 * 文件长度
	 */
	private Long size;
}
