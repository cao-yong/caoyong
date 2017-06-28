package com.caoyong;
/**
 * 
 * @author yong.cao
 * @time 2017年5月31日下午11:39:55
 */

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQuery;
import com.caoyong.core.dao.product.ProductDao;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-context.xml"})
public class TestProduct {
	
	@Autowired
	private ProductDao productDao;
	
	@Test
	public void testSelectByPrimaryKey(){
		Product product = productDao.selectByPrimaryKey(1L);
		System.out.println(product);
	}
	@Test
	public void testSelectByQuery(){
		ProductQuery query = new ProductQuery();
		query.setPageNo(1);
		query.setPageSize(1);
		query.setFields("id,name");
		List<Product> products = productDao.selectByExample(query);
		for (Product product : products) {
			System.out.println(product);
		}
	}
}