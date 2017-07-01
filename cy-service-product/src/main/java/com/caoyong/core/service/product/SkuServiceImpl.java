package com.caoyong.core.service.product;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caoyong.common.utlis.MoneyFormatUtil;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.bean.product.SkuQuery;
import com.caoyong.core.dao.product.ColorDao;
import com.caoyong.core.dao.product.SkuDao;
import com.caoyong.enums.ErrorCodeEnum;
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
	public ResultBase<List<Sku>> selectSkuByProductId(Long productId) throws BizException{
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
	/**
	 * 保存sku
	 * @param sku
	 * @throws BizException 
	 */
	@Override
	public ResultBase<Integer> addSku(Sku sku) throws BizException{
		log.info("addSku start. sku:{}", ToStringBuilder.reflectionToString
				(sku, ToStringStyle.DEFAULT_STYLE));
		ResultBase<Integer> result = new ResultBase<Integer>();
		try {
			//运费
			if(StringUtils.isNotBlank(sku.getDeliveFee())){
				sku.setDeliveFee(MoneyFormatUtil.format(sku.getDeliveFee()));
			}
			//市场价
			if(StringUtils.isNotBlank(sku.getMarketPrice())){
				sku.setMarketPrice(MoneyFormatUtil.format(sku.getMarketPrice()));
			}
			//售价
			if(StringUtils.isNotBlank(sku.getPrice())){
				sku.setPrice(MoneyFormatUtil.format(sku.getPrice()));
			}
			int count = skuDao.updateByPrimaryKeySelective(sku);
			result.setValue(count);
			result.setSuccess(true);
			log.info("result:{}", ToStringBuilder.reflectionToString
					(result, ToStringStyle.DEFAULT_STYLE));
		} catch (Exception e) {
			result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
			result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
			log.error("addSku error:{}", e.getMessage(), e);
			throw new BizException(ErrorCodeEnum.UNKOWN_ERROR,e.getMessage(),e);
		}
		log.info("addSku end.");
		return result;
	}
}
