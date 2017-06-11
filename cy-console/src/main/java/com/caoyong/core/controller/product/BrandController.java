package com.caoyong.core.controller.product;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.product.Brand;
import com.caoyong.core.bean.product.BrandQuery;
import com.caoyong.core.service.product.BrandService;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 品牌管理控制层
 * @author yong.cao
 * @time 2017年6月11日下午7:40:06
 */
@Slf4j
@Controller
public class BrandController {
	@Autowired
	private BrandService brandService;
	//查询
	@RequestMapping(value=("/brand/list.do"))
	public String list(BrandQuery query, Model model){
		log.info("query brand list start. query={}",ToStringBuilder.
				reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
		try {
			query.setPage(true);
			Page<Brand> page = brandService.selectPageByQuery(query);
			if(null!=page){
				model.addAttribute("page", page);
				model.addAttribute("name", query.getName());
				model.addAttribute("isDisplay", query.getIsDisplay());
				log.info("page = {}",ToStringBuilder.reflectionToString(page,
						ToStringStyle.DEFAULT_STYLE));
			}
		} catch (BizException e) {
			log.error("query list BizException:{}",e.getMessage(),e);
		}catch (Exception e) {
			log.error("query list Exception:{}",e.getMessage(),e);
		}
		log.info("query brand list end.");
		return "brand/list";
	}
	/**
	 * 去修改页
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value=("/brand/toEdit.do"))
	public String toEdit(Long id, Model model){
		log.info("toEdit start. id={}", id);
		try {
			Brand brand = brandService.selectBrandById(id);
			model.addAttribute("brand", brand);
		} catch (BizException e) {
			log.error("toEdit BizException:{}",e.getMessage(),e);
		}catch (Exception e) {
			log.error("toEdit Exception:{}",e.getMessage(),e);
		}
		log.info("toEdit end");
		return "brand/edit";
	}
}
