package com.caoyong.core.service.product;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQuery;
import com.caoyong.core.bean.product.ProductQuery.Criteria;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.core.dao.product.ProductDao;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 产品service
 * @author yong.cao
 * @time 2017年6月26日下午9:57:34
 */
@Slf4j
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductDao productDao;
	public Page<Product> selectPageByQuery(ProductQueryDTO query)throws BizException{
		log.info("selectPageByQuery start. query:{}",ToStringBuilder.
				reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
		Page<Product> page = new Page<>();
		StringBuilder params = new StringBuilder();
		//设置分页查询条件
		ProductQuery example = new ProductQuery();
		example.setPageNo(query.getPageNo());
		example.setPageSize(query.getLimit());
		example.setStartRow(query.getStart());
		//查询条件
		Criteria createCriteria = example.createCriteria();
		//设置查询条件及分页栏参数
		if(StringUtils.isNotBlank(query.getName())){
			createCriteria.andNameLike("%"+query.getName()+"%");
			params.append("name=").append(query.getName());
		}
		if(null != query.getBrandId()){
			createCriteria.andBrandIdEqualTo(query.getBrandId());
			params.append("&brandId=").append(query.getBrandId());
		}
		if(null != query.getIsShow()){
			createCriteria.andIsShowEqualTo(query.getIsShow());
			params.append("&isShow=").append(query.getIsShow());
		}else{
			//默认是下架
			createCriteria.andIsShowEqualTo(false);
			params.append("&isShow=").append(false);
		}
		try {
			Integer count = productDao.countByExample(example);
			List<Product> rows = productDao.selectByExample(example);
			//设置结果及分页对象
			if(null!=rows && !rows.isEmpty()){
				log.info("selectPageByQuery results:{}",count);
				log.info("selectPageByQuery rows:{}", ToStringBuilder.
						reflectionToString(rows, ToStringStyle.DEFAULT_STYLE));
				page.setStart(query.getStart());
				page.setResults(count);
				page.setLimit(query.getLimit());
				page.setPage(query.getPage());
				page.setPageNo(query.getPageNo());
				page.setRows(rows);
				page.setIsSuccess(true);
			}
		} catch (Exception e) {
			log.error("selectPageByQuery error:{}",e.getMessage(),e);
			page.setError("数据库查询产品分页失败");
			page.setErrorCode(e.getMessage());
			page.setErrorCode(e.getMessage());
			page.setResults(0);
		}
		log.info("selectPageByQuery end.");
		//分页展示
		String url = "/product/list.do";
		page.pageView(url, params.toString());
		return page;
	}
}
