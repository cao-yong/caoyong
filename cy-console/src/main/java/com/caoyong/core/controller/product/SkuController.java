package com.caoyong.core.controller.product;
/**
 * 库存管理
 * @author yong.cao
 * @time 2017年7月1日下午12:28:24
 */

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.service.product.SkuService;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class SkuController {
	@Autowired
	private SkuService skuService;
	
	@RequestMapping(value=("/sku/list.do"))
	public String list(Long productId,Model model){
		log.info("query list start");
		try {
			ResultBase<List<Sku>> result = skuService.selectSkuByProductId(productId);
			if(result.isSuccess()){
				List<Sku> skus = result.getValue();
				log.info("skus:{}", ToStringBuilder.
						reflectionToString(skus, ToStringStyle.DEFAULT_STYLE));
				model.addAttribute("skus",skus);
			}
		} catch (BizException e) {
			log.error("query list BizException:{}",e.getMessage(),e);
		} catch (Exception e) {
			log.error("query list Exception:{}",e.getMessage(),e);
		}
		log.info("query list end.");
		return "sku/list";
	}
}
