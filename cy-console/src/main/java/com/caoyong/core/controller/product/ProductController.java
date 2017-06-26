package com.caoyong.core.controller.product;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.core.service.product.ProductService;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 商品管理
 * @author yong.cao
 * @time 2017年6月27日上午12:05:57
 */
@Slf4j
@Controller
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@RequestMapping(value=("/product/list.do"))
	public String list(ProductQueryDTO query, Model model){
		log.info("query list start. query:{}", ToStringBuilder
				.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
		try {
			query.setPage(true);
			Page<Product> page = productService.selectPageByQuery(query);
			if(null!=page){
				model.addAttribute("page", page);
				model.addAttribute("name", query.getName());
				log.info("page = {}",page);
			}
		} catch (BizException e) {
			log.error("query list BizException:{}",e.getMessage(),e);
		}catch (Exception e) {
			log.error("query list Exception:{}",e.getMessage(),e);
		}
		log.info("query brand list end.");
		return "product/list";
	}
}
