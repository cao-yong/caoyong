package com.caoyong.core.controller.product;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.core.bean.base.BaseResponse;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Brand;
import com.caoyong.core.bean.product.BrandQuery;
import com.caoyong.core.service.product.BrandService;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 品牌管理控制层
 * 
 * @author yong.cao
 * @time 2017年6月11日下午7:40:06
 */
@RequestMapping(value = ("/brand"))
@Slf4j
@Controller
public class BrandController {

    @Reference(version = "1.0.0", timeout = 3000000)
    private BrandService brandService;

    //查询
    @RequestMapping(value = ("/brandList.do"))
    public String brandList(BrandQuery query, Model model) {
        log.info("query brandList start. query={}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        try {
            query.setPage(true);
            Page<Brand> page = brandService.selectPageByQuery(query);
            if (null != page) {
                page.setPage(query.getPage());
                model.addAttribute("page", page);
                model.addAttribute("name", query.getName());
                model.addAttribute("isDisplay", query.getIsDisplay());
                log.info("page = {}", page);
            }
        } catch (BizException e) {
            log.error("query brandList BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("query brandList Exception:{}", e.getMessage(), e);
        }
        log.info("query brandList end.");
        return "/product/brandList";
    }

    /**
     * 去品牌视图页面
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = ("/brandView{operation}.do"))
    public String brandView(BrandQuery query, Model model, @PathVariable String operation) {
        log.info("brandView start. query={}", ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        try {
            if (query.getId() != null) {
                ResultBase<Brand> result = brandService.selectBrandById(query.getId());
                if (result.isSuccess()) {
                    Brand brand = result.getValue();
                    if (null != brand) {
                        brand.setPageNo(query.getPageNo());
                    }
                    model.addAttribute("brand", brand);
                }
            }
        } catch (BizException e) {
            log.error("brandView BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("brandView Exception:{}", e.getMessage(), e);
        }
        log.info("brandView end");
        return "/product/brand" + operation;
    }

    /**
     * 保存商品
     * 
     * @param product
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ("/saveBrand.json"))
    public BaseResponse saveBrand(Brand brand) {
        BaseResponse resp = new BaseResponse();
        log.info("saveBrand start.brand:{}", ToStringBuilder.reflectionToString(brand, ToStringStyle.DEFAULT_STYLE));
        try {
            ResultBase<Integer> result = brandService.insertBrand(brand);
            log.info("resut:{}", ToStringBuilder.reflectionToString(result, ToStringBuilder.getDefaultStyle()));
            resp.setSuccess(result.isSuccess());
            resp.setCode(result.getErrorCode());
            resp.setMsg(result.getErrorMsg());
        } catch (Exception e) {
            log.error("saveBrand error:{}", e.getMessage(), e);
            resp.setCode(e.getMessage());
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        log.info("saveBrand end.");
        return resp;
    }

    /**
     * 修改
     * 
     * @param brand
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ("/updateBrand.json"))
    public BaseResponse updateBrand(Brand brand, Model model) {
        log.info("updateBrand start. brand={}", ToStringBuilder.reflectionToString(brand, ToStringStyle.DEFAULT_STYLE));
        BaseResponse resp = new BaseResponse();
        try {
            if (null != brand) {
                ResultBase<Integer> result = brandService.updateBrandById(brand);
                resp.setSuccess(result.isSuccess());
                resp.setCode(result.getErrorCode());
                resp.setMsg(result.getErrorMsg());
                log.info("result:{}", result);
            }
        } catch (BizException e) {
            log.error("updateBrand BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("updateBrand Exception:{}", e.getMessage(), e);
        }
        log.info("updateBrand end, resp:{}", ToStringBuilder.reflectionToString(resp, ToStringStyle.DEFAULT_STYLE));
        return resp;
    }

    /**
     * 删除品牌
     * 
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ("/deleteBrandById.json"))
    public BaseResponse deleteBrandById(Long id) {
        BaseResponse resp = new BaseResponse();
        log.info("deleteBrandById start.id:{}", id);
        try {
            ResultBase<Integer> result = brandService.deleteBrandById(id);
            log.info("resut:{}", ToStringBuilder.reflectionToString(result, ToStringBuilder.getDefaultStyle()));
            resp.setSuccess(result.isSuccess());
            resp.setCode(result.getErrorCode());
            resp.setMsg(result.getErrorMsg());
        } catch (Exception e) {
            log.error("deleteBrandById error:{}", e.getMessage(), e);
            resp.setCode(e.getMessage());
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        return resp;
    }

    /**
     * 批量删除
     * 
     * @param brand
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ("/deleteBrands.json"))
    public BaseResponse deletes(Long[] ids) {
        log.info("deletes start. ids={}", ToStringBuilder.reflectionToString(ids, ToStringStyle.DEFAULT_STYLE));
        BaseResponse resp = new BaseResponse();
        try {
            if (null == ids) {
                //防止对数据库多次无效交互
                log.error("ids can not be null");
                return null;
            }
            ResultBase<Integer> result = brandService.deletes(ids);
            resp.setSuccess(result.isSuccess());
            resp.setCode(result.getErrorCode());
            resp.setMsg(result.getErrorMsg());
            log.info("result:{}", result);
        } catch (BizException e) {
            log.error("deletes BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("deletes Exception:{}", e.getMessage(), e);
        }
        log.info("deletes end");
        return resp;
    }
}
