package com.caoyong.core.bean;

import java.util.Date;
/**
 * 测试表
 * @author yong.cao
 * @time 2017年5月31日下午11:26:42
 */
public class TestTb {
	private Integer id;
	private String name;
	private Date birthday;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	@Override
	public String toString() {
		return "TestTb [id=" + id + ", name=" + name + ", birthday=" + birthday + "]";
	}
}
