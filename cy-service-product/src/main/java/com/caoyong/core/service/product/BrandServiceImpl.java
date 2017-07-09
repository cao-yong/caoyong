package com.caoyong.core.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Brand;
import com.caoyong.core.bean.product.BrandQuery;
import com.caoyong.core.dao.product.BrandDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

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
	@Autowired
	private Jedis jedis;
	
	/**
	 * 查询分页对象
	 * @param query
	 * @return
	 */
	@Override
	public Page<Brand> selectPageByQuery(BrandQuery query)throws BizException{
		log.info("selectPageByQuery start, query = {}",
				ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
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
	public ResultBase<Brand> selectBrandById(Long id) throws BizException {
		ResultBase<Brand> result = new ResultBase<Brand>();
		log.info("selectBrandById start. id={}",id);
		Brand brand;
		try {
			brand = brandDao.selectBrandById(id);
			if(null!=brand){
				result.setValue(brand);
				result.setSuccess(true);
			}
			log.info("select brand:{}",ToStringBuilder.reflectionToString
					(brand, ToStringStyle.DEFAULT_STYLE));
		} catch (DataAccessException e) {
			result.setErrorCode(ErrorCodeEnum.DATA_BASE_ACCESS_ERROR.getCode());
			result.setErrorCode(ErrorCodeEnum.DATA_BASE_ACCESS_ERROR.getMsg());
			log.error("selectBrandById DataAccessException:{}",e.getMessage(),e);
			throw new BizException(ErrorCodeEnum.DATA_BASE_ACCESS_ERROR,e.getMessage(),e);
		} catch (Exception e) {
			result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
			result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
			log.error("selectBrandById Exception:{}",e.getMessage(),e);
			throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(),e);
		}
		log.info("selectBrandById end, resut:{}",ToStringBuilder.
				reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
		return result;
	}

	@Override
	public ResultBase<Integer> updateBrandById(Brand brand) throws BizException{
		ResultBase<Integer> result = new ResultBase<Integer>();
		result.setValue(0);
		log.info("updateBrandById start. brand:{}", ToStringBuilder.reflectionToString
				(brand, ToStringStyle.DEFAULT_STYLE));
		try {
			//保存品牌到redis
			jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());
			brandDao.updateBrandById(brand);
			result.setValue(1);
		} catch (DataAccessException e) {
			result.setErrorCode(ErrorCodeEnum.DATA_BASE_ACCESS_ERROR.getCode());
			result.setErrorMsg(ErrorCodeEnum.DATA_BASE_ACCESS_ERROR.getMsg());
			log.error("updateBrandById DataAccessException:{}",e.getMessage(),e);
			throw new BizException(ErrorCodeEnum.DATA_BASE_ACCESS_ERROR,e.getMessage(),e);
		} catch (Exception e) {
			result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
			result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
			log.error("updateBrandById Exception:{}",e.getMessage(),e);
			throw new BizException(ErrorCodeEnum.UNKOWN_ERROR,e.getMessage(),e);
		}
		log.info("updateBrandById end, resut:{}",ToStringBuilder.
				reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
		return result;
	}

	@Override
	public ResultBase<Integer> deletes(Long[] ids) throws BizException {
		log.info("deletes start. ids:{}", ToStringBuilder.
				reflectionToString(ids, ToStringStyle.DEFAULT_STYLE));
		ResultBase<Integer> result = new ResultBase<Integer>();
		result.setSuccess(false);
		result.setValue(0);
		try {
			if(null != ids){
				brandDao.deletes(ids);
				result.setValue(ids.length);
			}
		} catch (DataAccessException e) {
			log.error("deletes DataAccessException:{}", e.getMessage(),e);
			result.setErrorCode(ErrorCodeEnum.DATA_BASE_ACCESS_ERROR.getCode());
			result.setErrorMsg(ErrorCodeEnum.DATA_BASE_ACCESS_ERROR.getMsg());
			throw new BizException(ErrorCodeEnum.DATA_BASE_ACCESS_ERROR,e.getMessage(),e);
		} catch (Exception e) {
			log.error("deletes Exception:{}",e.getMessage(),e);
			result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
			result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
			throw new BizException(ErrorCodeEnum.UNKOWN_ERROR,e.getMessage(),e);
		}
		log.info("deletes end result:{}", ToStringBuilder.
				reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
		return result;
	}

	@Override
	public List<Brand> selectListByQuery(Integer isDisplay) throws BizException {
		log.info("selectPageByQuery start. isDisplya=", isDisplay);
		BrandQuery query = new BrandQuery();
		query.setIsDisplay(isDisplay);
		try {
			List<Brand> list = brandDao.selectBrandListByQuery(query);
			//打印日志，最多打印10条
			log.info("success, list:{}", ToStringBuilder.
					reflectionToString(null==list ? "null" : 
			list.subList(0, Math.min(list.size(), 10)), ToStringStyle.DEFAULT_STYLE));
			return list;
		} catch (DataAccessException e) {
			log.error("selectPageByQuery DataAccessException:{}", e.getMessage(),e);
		} catch (Exception e) {
			log.error("selectPageByQuery Exception:{}",e.getMessage(),e);
		}
		return null;
	}
	
	@Override
	public List<Brand> selectBrandListFromRedis() throws BizException{
		log.info("selectBrandListFromRedis start.");
		List<Brand> brands = new ArrayList<>();
		try {
			Map<String, String> hgetAll = jedis.hgetAll("brand");
			Set<Entry<String, String>> entrySet = hgetAll.entrySet();
			//遍历取值
			for (Entry<String, String> entry : entrySet) {
				Brand brand = new Brand();
				brand.setId(Long.parseLong(entry.getKey()));;
				brand.setName(entry.getValue());
				brands.add(brand);
			}
		} catch (NumberFormatException e) {
			log.error("selectBrandListFromRedis parsLong error:", e.getMessage() ,e);
		}catch (Exception e) {
			log.error("selectBrandListFromRedis error:", e.getMessage(), e);
		}
		log.info("selectBrandListFromRedis end.");
		return brands;
	}
	
}
