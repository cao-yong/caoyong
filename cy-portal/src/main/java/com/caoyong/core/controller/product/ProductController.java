package com.caoyong.core.controller.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Brand;
import com.caoyong.core.bean.product.Color;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.service.CmsService;
import com.caoyong.core.service.SearchService;
import com.caoyong.core.service.product.BrandService;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 前台商品
 * 
 * @author yong.cao
 * @time 2017年7月2日下午3:33:18
 */

@Slf4j
@Controller
public class ProductController {
    @Reference(version = "1.0.0")
    private SearchService searchService;
    @Reference(version = "1.0.0")
    private BrandService  brandService;
    @Reference(version = "1.0.0")
    private CmsService    cmsService;

    /**
     * 首页
     * 
     * @return
     */
    @RequestMapping(value = ("/"))
    public String index() {
        return "index";
    }

    /**
     * 搜索
     * 
     * @return
     */
    @RequestMapping(value = ("/search"))
    public String search(ProductQueryDTO query, Model model) {
        log.info("search start. keyword:{}", ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        try {
            //从redis中查询品牌
            List<Brand> brands = brandService.selectBrandListFromRedis();
            model.addAttribute("brands", brands);
            model.addAttribute("keyword", query.getKeyword());
            model.addAttribute("brandId", query.getBrandId());
            model.addAttribute("price", query.getPrice());
            Page<Product> page = searchService.selectPageByQuery(query);
            if (null != page.getRows() && !page.getRows().isEmpty()) {
                model.addAttribute("page", page);
            }
            //设置已选条件
            Map<String, String> selectedTermsMap = new HashMap<>();
            //设置品牌
            if (null != query.getBrandId()) {
                String brandName = brands.stream().filter(brand -> query.getBrandId().equals(brand.getId())).findFirst()
                        .orElseGet(Brand::new).getName();
                selectedTermsMap.put("品牌", brandName);
            }
            //设置价格
            if (StringUtils.isNotBlank(query.getPrice())) {
                if (query.getPrice().contains("-")) {
                    selectedTermsMap.put("价格", query.getPrice());
                } else {
                    selectedTermsMap.put("价格", query.getPrice() + "以上");
                }
            }
            model.addAttribute("selectedTermsMap", selectedTermsMap);
        } catch (BizException e) {
            log.error("search biz error:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("search error:{}", e.getMessage(), e);
        }
        log.info("search end.");
        return "search";
    }

    /**
     * 去商品详情页
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = ("/product/detail"))
    public String detail(Long id, Model model) {
        log.info("detail start id:{}", id);
        try {
            //商品信息
            ResultBase<Product> productResult = cmsService.selectProductById(id);
            if (productResult.isSuccess() && null != productResult.getValue()) {
                model.addAttribute("product", productResult.getValue());
            }
            //sku信息
            ResultBase<List<Sku>> skusResult = cmsService.selectSkuListByProductId(id);
            if (skusResult.isSuccess() && null != skusResult.getValue()) {
                model.addAttribute("skus", skusResult.getValue());
                //获取颜色,并去重 ,java8函数式编程实现
                //				Set<Color> colors = new HashSet<Color>();
                //				for(Sku sku : skusResult.getValue()){
                //					colors.add(sku.getColor());
                //				}
                List<Color> colors = skusResult.getValue().stream().map(Sku::getColor).distinct()
                        .collect(Collectors.toList());
                model.addAttribute("colors", colors);
            }
        } catch (BizException e) {
            log.error("detail biz error:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("detail error:{}", e.getMessage(), e);
        }
        log.info("detail end.");
        return "product";
    }
}
