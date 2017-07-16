package com.caoyong.core.service.user;

import org.springframework.beans.factory.annotation.Autowired;

import com.caoyong.common.web.Constants;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * 保存用户信息到redis
 * 实现session共享
 * @author yong.cao
 * @time 2017年7月16日下午1:31:55
 */
@Slf4j
public class SessionProviderImpl implements SessionProvider{
	@Autowired
	private Jedis jedis;
	//失效时间，单位分钟
	private Integer expire = 30;
	
	@Override
	public void setAttributeForUser(String name, String value) {
		log.info("setAttributeForUser start.name:{},value:{}",name,value);
		try {
			//保存到redis中
			String key = name + ":" +Constants.USER_NAME;
			jedis.set(key, value);
			jedis.expire(key, expire * 60);
		} catch (Exception e) {
			log.error("setAttributeForUser error:", e.getMessage(), e);
		}
		log.info("setAttributeForUser end.");
	}

	@Override
	public String getAttributeForUser(String name) {
		log.info("getAttributeForUser start, name:{}", name);
		String value = null;
		try {
			String key = name + ":" +Constants.USER_NAME;
			value = jedis.get(key);
			//重置失效时间
			if(null !=value){
				jedis.expire(key, expire * 60);
			}
		} catch (Exception e) {
			log.error("getAttributeForUser error:", e.getMessage(), e);
		}
		log.info("getAttributeForUser end.");
		return value;
	}

	public void setExpire(Integer expire) {
		if(null !=expire){
			this.expire = expire;
		}
	}
}
