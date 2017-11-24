package com.caoyong.core.controller.product;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.enums.ProductIsShowEnum;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Brand;
import com.caoyong.core.bean.product.Color;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.core.service.product.BrandService;
import com.caoyong.core.service.product.ProductService;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 商品管理
 * 
 * @author yong.cao
 * @time 2017年6月27日上午12:05:57
 */

@Slf4j
@Controller
@RequestMapping(value = "/product")
public class ProductController {

    @Reference(version = "1.0.0")
    private ProductService productService;
    @Reference(version = "1.0.0")
    private BrandService   brandService;

    /**
     * 商品分页
     * 
     * @param query
     * @param model
     * @return
     */
    @RequestMapping(value = ("/productList.do"))
    public String list(ProductQueryDTO query, Model model) {
        log.info("query list start. query:{}", ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        try {
            //查询品牌结果集
            List<Brand> brands = brandService.selectListByQuery(1);
            model.addAttribute("brands", brands);
            model.addAttribute("showTypes", ProductIsShowEnum.values());
            query.setPage(true);
            Page<Product> page = productService.selectPageByQuery(query);
            if (null != page) {
                model.addAttribute("page", page);
                model.addAttribute("name", query.getName());
                model.addAttribute("brandId", query.getBrandId());
                log.info("page = {}", page);
            }
            if (query.getIsShow() != null) {
                model.addAttribute("isShowCode", query.getIsShow() ? 1 : 0);
            }
        } catch (BizException e) {
            log.error("query list BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("query list Exception:{}", e.getMessage(), e);
        }
        log.info("query brand list end.");
        return "/product/productList";
    }

    /**
     * 去商品添加页面
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = ("/addProduct.do"))
    public String addProduct(Model model) {
        log.info("addProduct start.");
        try {
            //查询品牌结果集
            List<Brand> brands = brandService.selectListByQuery(1);
            model.addAttribute("brands", brands);
            //查询颜色结果集
            ResultBase<List<Color>> colorResult = productService.selectColorList();
            log.info("colorResult:{}", ToStringBuilder.reflectionToString(colorResult, ToStringStyle.DEFAULT_STYLE));
            if (colorResult.isSuccess()) {
                List<Color> colors = colorResult.getValue();
                model.addAttribute("colors", colors);
            }
        } catch (BizException e) {
            log.error("addProduct BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("addProduct Exception:{}", e.getMessage(), e);
        }
        log.info("addProduct end.");
        return "/product/productForm";
    }

    /**
     * 保存商品
     * 
     * @param product
     * @return
     */
    @RequestMapping(value = ("/add.do"))
    public String add(Product product) {
        log.info("add start.product:{}", ToStringBuilder.reflectionToString(product, ToStringStyle.DEFAULT_STYLE));
        try {
            ResultBase<Integer> result = productService.saveProduct(product);
            log.info("resut:{}", ToStringBuilder.reflectionToString(result, ToStringBuilder.getDefaultStyle()));
        } catch (Exception e) {
            log.error("add error:{}", e.getMessage(), e);
        }

        return "redirect:/product/list.do";
    }

    /**
     * 上架
     * 
     * @param ids
     * @return
     */
    @RequestMapping(value = ("/isShow"))
    public String isShow(Long[] ids) {
        log.info("isShow start. ids:{}", ToStringBuilder.reflectionToString(ids, ToStringStyle.DEFAULT_STYLE));
        try {
            ResultBase<Integer> result = productService.isShow(ids);
            log.info("result:{}", ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        } catch (BizException e) {
            log.error("isShow biz error:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("isShow error:{}", e.getMessage(), e);
        }
        return "forward:/product/list.do";
    }
}
