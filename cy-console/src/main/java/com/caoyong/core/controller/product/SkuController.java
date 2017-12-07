package com.caoyong.core.controller.product;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.BaseResponse;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.bean.product.SkuDTO;
import com.caoyong.core.bean.product.SkuQueryDTO;
import com.caoyong.core.service.product.SkuService;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
/**
 * 库存管理
 * 
 * @author yong.cao
 * @time 2017年7月30日 下午9:38:51
 */
@RequestMapping(value = ("/sku"))
public class SkuController {

    @Reference(version = "1.0.0", timeout = 3000000)
    private SkuService skuService;

    @RequestMapping(value = ("/skuList.do"))
    public String skuList(SkuQueryDTO query, Model model) {
        log.info("query skuList start");
        try {
            Page<Sku> page = skuService.selectPageByQuery(query);
            if (page.getIsSuccess()) {
                log.info("page:{}", ToStringBuilder.reflectionToString(page, ToStringStyle.DEFAULT_STYLE));
                model.addAttribute("page", page);
            }
        } catch (BizException e) {
            log.error("query skuList BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("query skuList Exception:{}", e.getMessage(), e);
        }
        log.info("query skuList end.");
        return "/product/skuList";
    }

    /**
     * 保存sku
     * 
     * @param sku
     * @param response
     */
    @RequestMapping(value = ("/addSku.do"))
    public void addSku(Sku sku, HttpServletResponse response) {
        log.info("addSku start sku:{}", ToStringBuilder.reflectionToString(sku, ToStringStyle.DEFAULT_STYLE));
        try {
            ResultBase<Integer> result = skuService.addSku(sku);
            //返回数据
            JSONObject jsonObj = new JSONObject();
            if (result.isSuccess()) {
                jsonObj.put("success", true);
                jsonObj.put("msg", Constants.SUCCESS);
            } else {
                jsonObj.put("success", false);
                jsonObj.put("errorMsg", result.getErrorMsg());
            }
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonObj.toString());
        } catch (BizException e) {
            log.error("addSku BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("addSku Exception:{}", e.getMessage(), e);
        }
    }

    /**
     * 操作sku
     * 
     * @param product
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ("/operatingSku.json"))
    public BaseResponse operatingSku(SkuDTO skuDTO) {
        BaseResponse resp = new BaseResponse();
        log.info("operatingSku start.product:{}",
                ToStringBuilder.reflectionToString(skuDTO, ToStringStyle.DEFAULT_STYLE));
        try {
            ResultBase<Integer> result = skuService.operatingSkuBySkuDTO(skuDTO);
            log.info("resut:{}", ToStringBuilder.reflectionToString(result, ToStringBuilder.getDefaultStyle()));
            resp.setSuccess(result.isSuccess());
            resp.setCode(result.getErrorCode());
        } catch (Exception e) {
            log.error("saveProduct error:{}", e.getMessage(), e);
            resp.setCode(e.getMessage());
            resp.setMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
        }
        log.info("operatingSku end.");
        return resp;
    }
}
