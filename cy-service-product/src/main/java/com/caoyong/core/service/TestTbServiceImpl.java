package com.caoyong.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caoyong.core.bean.TestTb;
import com.caoyong.core.dao.TestTbDao;

/**
 * 
 * @author yong.cao
 * @time 2017年6月1日下午9:46:37
 */
@Service
@Transactional
public class TestTbServiceImpl implements TestTbService{
	@Autowired
	private TestTbDao testTbDao;

	@Override
	public void insertTestTb(TestTb testTb){
		testTbDao.insertTestTb(testTb);
		throw new RuntimeException();
	}
}
