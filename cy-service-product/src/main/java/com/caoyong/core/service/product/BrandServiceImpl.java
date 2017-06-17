package com.caoyong.core.service.product;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.product.Brand;
import com.caoyong.core.bean.product.BrandQuery;
import com.caoyong.core.dao.product.BrandDao;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 品牌管理
 * @author yong.cao
 * @time 2017年6月10日下午1:41:42
 */
@Service("brandService")
@Transactional
@Slf4j
public class BrandServiceImpl implements BrandService{
	@Autowired
	private BrandDao brandDao;

	/**
	 * 查询分页对象
	 * @param query
	 * @return
	 */
	@Override
	public Page<Brand> selectPageByQuery(BrandQuery query)throws BizException{
		log.info("selectPageByQuery start, query = {}",
				ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
		//每页数
		query.setLimit(10);
		//当前页
		query.setPageNo((null == query.getPageNo() || query.getPageNo()<1)? 
				1 : query.getPageNo());
		//参数
		StringBuilder params = new StringBuilder();
		//默认是显示
		if(null != query.getName()){
			params.append("name=").append(query.getName());
		}
		if(null == query.getIsDisplay()){
			query.setIsDisplay(1);
			params.append("&isDisplay=").append(1);
		}else{
			params.append("&isDisplay=").append(query.getIsDisplay());
		}
		Page<Brand> page = new Page<Brand>();
		page.setIsSuccess(false);
		try {
			Integer count = brandDao.selectCount(query);
			List<Brand> rows = brandDao.selectBrandListByQuery(query);
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
		} catch (DataAccessException e) {
			log.error("selectPageByQuery DataAccessException:{}",e.getMessage(),e);
			page.setError("数据库查询产品分页失败");
			page.setErrorCode(e.getMessage());
			page.setErrorCode(e.getMessage());
			page.setResults(0);
			return page;
		}catch (Exception e) {
			log.error("selectPageByQuery error:{}",e.getMessage(),e);
			page.setError("数据库查询产品分页失败");
			page.setErrorCode(e.getMessage());
			page.setErrorCode(e.getMessage());
			page.setResults(0);
		}
		log.info("selectPageByQuery end.");
		//分页展示
		String url = "/brand/list.do";
		page.pageView(url, params.toString());
		return page;
	}

	@Override
	public Brand selectBrandById(Long id) throws BizException {
		log.info("selectBrandById start. id={}",id);
		Brand brand = null;
		try {
			brand = brandDao.selectBrandById(id);
			log.info("select brand:{}",ToStringBuilder.reflectionToString
					(brand, ToStringStyle.DEFAULT_STYLE));
		} catch (DataAccessException e) {
			log.error("selectBrandById DataAccessException:{}",e.getMessage(),e);
		} catch (Exception e) {
			log.error("selectBrandById Exception:{}",e.getMessage(),e);
		}
		log.info("selectBrandById end");
		return brand;
	}
	
}
