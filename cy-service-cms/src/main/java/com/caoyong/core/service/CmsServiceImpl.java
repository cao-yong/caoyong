package com.caoyong.core.service;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.bean.product.SkuQuery;
import com.caoyong.core.dao.product.ColorDao;
import com.caoyong.core.dao.product.ProductDao;
import com.caoyong.core.dao.product.SkuDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 评论、晒单、广告、静态化
 * @author yong.cao
 * @time 2017年7月11日下午9:41:02
 */
@Slf4j
@Service("cmsService")
public class CmsServiceImpl implements CmsService{
	@Autowired
	private ProductDao productDao;
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
	
	@Override
	public ResultBase<Product> selectProductById(Long productId) throws BizException{
		log.info("selectProductById start. productId:{}", productId);
		ResultBase<Product> result = new ResultBase<Product>();
		try {
			Product product = productDao.selectByPrimaryKey(productId);
			result.setValue(product);
			result.setSuccess(true);
			log.info("result:{}", ToStringBuilder.reflectionToString
					(product, ToStringStyle.DEFAULT_STYLE));
		} catch (Exception e) {
			result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
			result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
			log.error(ErrorCodeEnum.UNKOWN_ERROR.getMsg(), e.getMessage(), e);
		}
		log.info("selectProductById end.");
		return result;
	}
	@Override
	public ResultBase<List<Sku>> selectSkuListByProductId(Long productId){
		log.info("selectSkuListByProductId start. productId:{}", productId);
		ResultBase<List<Sku>> result= new ResultBase<List<Sku>>();;
		try {
			//查询sku结果集
			SkuQuery example = new SkuQuery();
			//查询库存大于0
			example.createCriteria().andIsDeletedEqualTo(Constants.CONSTANTS_N)
			.andProductIdEqualTo(productId).andStockGreaterThan(0);
			List<Sku> skus = skuDao.selectByExample(example);
			//查询color
			if(null != skus && !skus.isEmpty()){
				for(Sku sku : skus){
					sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
				}
				result.setValue(skus);
			}
			result.setSuccess(true);
		} catch (Exception e) {
			result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
			result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
			log.error("selectSkuListByProductId error:", e.getMessage(), e);
		}
		log.info("selectSkuListByProductId end.");
		return result;
	}
}
