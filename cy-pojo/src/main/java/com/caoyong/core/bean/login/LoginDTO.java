package com.caoyong.core.bean.login;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 登录
 * @author yong.cao
 * @time 2017年7月16日上午9:53:32
 */
@Setter
@Getter
@ToString
public class LoginDTO implements Serializable{
	
	private static final long serialVersionUID = 3508874430712524028L;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 回显url
	 */
	private String returnUrl;
}
