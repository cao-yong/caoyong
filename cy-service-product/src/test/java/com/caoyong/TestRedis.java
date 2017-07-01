package com.caoyong;
/**
 * 测试redis
 * @author yong.cao
 * @time 2017年6月30日下午10:49:33
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-context.xml"})
public class TestRedis {
	@Autowired
	private Jedis jedis;
	@Test
	public void testRedis() throws Exception {
		Jedis jedis = new Jedis("192.168.128.128",6379);
		Long pno = jedis.incr("pno");
		System.out.println(String.valueOf(pno));
		jedis.close();
	}
	
	@Test
	public void testSpringJedis() throws Exception {
		Long pno = jedis.decr("pno");
		System.out.println(String.valueOf(pno));
		jedis.close();
	}
}
