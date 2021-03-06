package com.caoyong.core.controller.product;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.enums.ProductIsShowEnum;
import com.caoyong.common.enums.ProductSizesEnum;
import com.caoyong.core.bean.base.BaseResponse;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Brand;
import com.caoyong.core.bean.product.Color;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductIsShowVO;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.core.service.product.BrandService;
import com.caoyong.core.service.product.ProductService;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 商品管理
 * 
 * @author yong.cao
 * @since 2017年6月27日上午12:05:57
 */

@Slf4j
@Controller
@RequestMapping(value = "/product")
public class ProductController {

    @Reference(version = "1.0.0", timeout = 3000000)
    private ProductService productService;
    @Reference(version = "1.0.0", timeout = 3000000)
    private BrandService   brandService;

    /**
     * 商品分页
     * 
     * @param query 查询条件
     * @param model 数据模型
     * @return 视图名称
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
     * 去商品视图页面
     * 
     * @param model 数据模型
     * @param id id
     * @return 视图名称
     */
    @RequestMapping(value = ("/productView{operation}.do"))
    public String productView(Model model, Long id, @PathVariable String operation) {
        log.info("productView start. id:{}, operation:{}", id, operation);
        try {
            //查询品牌结果集
            List<Brand> brands = brandService.selectListByQuery(1);
            model.addAttribute("brands", brands);
            model.addAttribute("productSizes", ProductSizesEnum.values());
            //查询颜色结果集
            ResultBase<List<Color>> colorResult = productService.selectColorList();
            log.info("colorResult:{}", ToStringBuilder.reflectionToString(colorResult, ToStringStyle.DEFAULT_STYLE));
            if (colorResult.isSuccess()) {
                List<Color> colors = colorResult.getValue();
                model.addAttribute("colors", colors);
            }
            if (null != id) {
                //查询产品
                ResultBase<Product> productResult = productService.selectProductById(id);
                if (productResult.isSuccess() && null != productResult.getValue()) {
                    Product product = productResult.getValue();
                    model.addAttribute("product", product);
                }
            }
        } catch (BizException e) {
            log.error("productView BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("productView Exception:{}", e.getMessage(), e);
        }
        log.info("productView end.");
        return "/product/product" + operation;
    }

    /**
     * 保存商品
     * 
     * @param product 产品
     * @return 结果
     */
    @ResponseBody
    @RequestMapping(value = ("/saveProduct.json"))
    public BaseResponse saveProduct(Product product) {
        BaseResponse resp = new BaseResponse();
        log.info("saveProduct start.product:{}",
                ToStringBuilder.reflectionToString(product, ToStringStyle.DEFAULT_STYLE));
        try {
            ResultBase<Integer> result = productService.saveProduct(product);
            log.info("resut:{}", ToStringBuilder.reflectionToString(result, ToStringBuilder.getDefaultStyle()));
            resp.setSuccess(result.isSuccess());
            resp.setCode(result.getErrorCode());
        } catch (Exception e) {
            log.error("saveProduct error:{}", e.getMessage(), e);
            resp.setCode(e.getMessage());
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        log.info("saveProduct end.");
        return resp;
    }

    /**
     * 修改商品
     * 
     * @param product 产品
     * @return 结果
     */
    @ResponseBody
    @RequestMapping(value = ("/updateProduct.json"))
    public BaseResponse updateProduct(Product product) {
        BaseResponse resp = new BaseResponse();
        log.info("updateProduct start.product:{}",
                ToStringBuilder.reflectionToString(product, ToStringStyle.DEFAULT_STYLE));
        try {
            ResultBase<Integer> result = productService.updateProductById(product);
            log.info("resut:{}", ToStringBuilder.reflectionToString(result, ToStringBuilder.getDefaultStyle()));
            resp.setSuccess(result.isSuccess());
            resp.setCode(result.getErrorCode());
        } catch (Exception e) {
            log.error("updateProduct error:{}", e.getMessage(), e);
            resp.setCode(e.getMessage());
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        log.info("updateProduct end.");
        return resp;
    }

    /**
     * 删除商品
     * 
     * @param id id
     * @return 结果
     */
    @ResponseBody
    @RequestMapping(value = ("/deleteProduct.json"))
    public BaseResponse deleteProduct(Long id) {
        BaseResponse resp = new BaseResponse();
        log.info("deleteProduct start.id:{}", id);
        try {
            ResultBase<Integer> result = productService.deleteProductById(id);
            log.info("resut:{}", ToStringBuilder.reflectionToString(result, ToStringBuilder.getDefaultStyle()));
            resp.setSuccess(result.isSuccess());
            resp.setCode(result.getErrorCode());
        } catch (Exception e) {
            log.error("deleteProduct error:{}", e.getMessage(), e);
            resp.setCode(e.getMessage());
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        return resp;
    }

    /**
     * 修改商品
     * @param product 产品
     * @return 结果
     */
    @ResponseBody
    @RequestMapping(value = ("/editProduct.json"))
    public BaseResponse editProduct(Product product) {
        BaseResponse resp = new BaseResponse();
        log.info("editProduct start.product:{}",
                ToStringBuilder.reflectionToString(product, ToStringStyle.DEFAULT_STYLE));
        try {
            ResultBase<Integer> result = productService.updateProductById(product);
            log.info("resut:{}", ToStringBuilder.reflectionToString(result, ToStringBuilder.getDefaultStyle()));
            resp.setSuccess(result.isSuccess());
            resp.setCode(result.getErrorCode());
        } catch (Exception e) {
            log.error("editProduct error:{}", e.getMessage(), e);
            resp.setCode(e.getMessage());
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        return resp;
    }

    /**
     * 批量删除商品
     * 
     * @param ids ids
     * @return 结果
     */
    @ResponseBody
    @RequestMapping(value = ("/deleteProducts.json"))
    public BaseResponse deleteProducts(Long[] ids) {
        BaseResponse resp = new BaseResponse();
        log.info("deleteProducts start.ids:{}",
                ToStringBuilder.reflectionToString(ids, ToStringBuilder.getDefaultStyle()));
        try {
            ResultBase<Integer> result = productService.deleteProductByIds(ids);
            log.info("resut:{}", ToStringBuilder.reflectionToString(result, ToStringBuilder.getDefaultStyle()));
            resp.setSuccess(result.isSuccess());
            resp.setCode(result.getErrorCode());
        } catch (Exception e) {
            log.error("deleteProducts error:{}", e.getMessage(), e);
            resp.setCode(e.getMessage());
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        return resp;
    }

    /**
     * 上架
     * @param isShowVO 产品是否上架VO
     * @return 结果
     */
    @RequestMapping(value = ("/isShow.json"))
    @ResponseBody
    public BaseResponse isShow(@RequestBody ProductIsShowVO isShowVO) {
        log.info("isShow start. ids:{}", ToStringBuilder.reflectionToString(isShowVO, ToStringStyle.DEFAULT_STYLE));
        BaseResponse resp = new BaseResponse();
        try {
            ResultBase<Integer> result = productService.isShow(isShowVO);
            log.info("result:{}", ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
            resp.setSuccess(result.isSuccess());
            resp.setCode(result.getErrorCode());
        } catch (BizException e) {
            log.error("isShow biz error:{}", e.getMessage(), e);
            resp.setCode(e.getMessage());
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        } catch (Exception e) {
            log.error("isShow error:{}", e.getMessage(), e);
            resp.setCode(e.getMessage());
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        return resp;
    }
}
