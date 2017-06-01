package com.caoyong;
/**
 * 
 * @author yong.cao
 * @time 2017年5月31日下午11:39:55
 */

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caoyong.core.bean.TestTb;
import com.caoyong.core.dao.TestTbDao;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-context.xml"})
public class TestTbTest {
	
	@Autowired
	private TestTbDao testTbDao;
	
	@Test
	public void testAdd(){
		TestTb testTb = new TestTb();
		testTb.setName("刘一手");
		testTb.setBirthday(new Date());
		testTbDao.insertTestTb(testTb);
	}
}
