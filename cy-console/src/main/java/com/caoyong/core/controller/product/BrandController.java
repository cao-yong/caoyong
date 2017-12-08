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
    public String brandView(Long id, Model model, @PathVariable String operation) {
        log.info("brandView start. id={}", id);
        try {
            ResultBase<Brand> result = brandService.selectBrandById(id);
            if (result.isSuccess()) {
                Brand brand = result.getValue();
                model.addAttribute("brand", brand);
            }
        } catch (BizException e) {
            log.error("brandView BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("brandView Exception:{}", e.getMessage(), e);
        }
        log.info("brandView end");
        return "brand/brand" + operation;
    }

    /**
     * 修改
     * 
     * @param brand
     * @param model
     * @return
     */
    @RequestMapping(value = ("/edit.do"))
    public String edit(Brand brand, Model model) {
        log.info("edit start. brand={}", ToStringBuilder.reflectionToString(brand, ToStringStyle.DEFAULT_STYLE));
        try {
            if (null != brand) {
                ResultBase<Integer> result = brandService.updateBrandById(brand);
                log.info("result:{}", result);
            }
        } catch (BizException e) {
            log.error("toEdit BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("toEdit Exception:{}", e.getMessage(), e);
        }
        log.info("edit end");
        return "redirect:/brand/list.do";
    }

    /**
     * 删除品牌
     * 
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ("/deleteBrandById.json"))
    public BaseResponse deleteProduct(Long id) {
        BaseResponse resp = new BaseResponse();
        log.info("deleteBrandById start.id:{}", id);
        try {
            ResultBase<Integer> result = brandService.deleteBrandById(id);
            log.info("resut:{}", ToStringBuilder.reflectionToString(result, ToStringBuilder.getDefaultStyle()));
            resp.setSuccess(result.isSuccess());
            resp.setCode(result.getErrorCode());
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
    @RequestMapping(value = ("/deletes.do"))
    public String deletes(Long[] ids) {
        log.info("deletes start. ids={}", ToStringBuilder.reflectionToString(ids, ToStringStyle.DEFAULT_STYLE));
        try {
            if (null == ids) {
                //防止对数据库多次无效交互
                log.error("ids can not be null");
                return null;
            }
            ResultBase<Integer> result = brandService.deletes(ids);
            log.info("result:{}", result);
        } catch (BizException e) {
            log.error("deletes BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("deletes Exception:{}", e.getMessage(), e);
        }
        log.info("deletes end");
        return "forward:/brand/list.do";
    }
}
