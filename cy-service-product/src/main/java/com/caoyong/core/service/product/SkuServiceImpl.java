package com.caoyong.core.service.product;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.bean.product.SkuQuery;
import com.caoyong.core.dao.product.ColorDao;
import com.caoyong.core.dao.product.SkuDao;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
	@Override
	public ResultBase<List<Sku>> selectSkuByProductId(Long productId)throws BizException{
		log.info("selectSkuByProductId start. productId:{}", productId);
		ResultBase<List<Sku>> result = new ResultBase<List<Sku>>();
		try {
			SkuQuery example = new SkuQuery();
			example.createCriteria().andIsDeletedEqualTo(Constants.CONSTANTS_N)
			.andProductIdEqualTo(productId);
			List<Sku> skus = skuDao.selectByExample(example);
			if(null != skus && !skus.isEmpty()){
				//循环
				skus.stream().forEach(x->{
					x.setColor(colorDao.selectByPrimaryKey(x.getColorId()));
				});
			}
			if(null !=skus && !skus.isEmpty()){
				result.setSuccess(true);
				result.setValue(skus);
			}
			log.info("result:{}", ToStringBuilder.reflectionToString
					(result, ToStringStyle.DEFAULT_STYLE));
		} catch (Exception e) {
			log.error("selectSkuByProductId error:", e.getMessage(), e);
			result.setErrorCode(e.getMessage());
			result.setErrorMsg("查询sku失败");
		}
		log.info("selectSkuByProductId end");
		return result;
	}
}
