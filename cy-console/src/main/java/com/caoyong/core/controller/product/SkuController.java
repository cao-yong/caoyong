package com.caoyong.core.controller.product;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.service.product.SkuService;
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
public class SkuController {

    @Autowired
    private SkuService skuService;

    @RequestMapping(value = ("/sku/list.do"))
    public String list(Long productId, Model model) {
        log.info("query list start");
        try {
            ResultBase<List<Sku>> result = skuService.selectSkuByProductId(productId);
            if (result.isSuccess()) {
                List<Sku> skus = result.getValue();
                log.info("skus:{}", ToStringBuilder.reflectionToString(skus, ToStringStyle.DEFAULT_STYLE));
                model.addAttribute("skus", skus);
            }
        } catch (BizException e) {
            log.error("query list BizException:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("query list Exception:{}", e.getMessage(), e);
        }
        log.info("query list end.");
        return "sku/list";
    }

    /**
     * 保存sku
     * 
     * @param sku
     * @param response
     */
    @RequestMapping(value = ("/sku/addSku.do"))
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
}
