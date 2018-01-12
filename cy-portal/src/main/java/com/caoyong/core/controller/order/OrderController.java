package com.caoyong.core.controller.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.utlis.RequestUtil;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.order.Order;
import com.caoyong.core.service.order.OrderService;
import com.caoyong.core.service.user.SessionProvider;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单controller
 * 
 * @author yong.cao
 * @time 2017年7月23日下午4:55:53
 */

@Slf4j
@Controller
public class OrderController {
    @Reference(version = "1.0.0")
    private OrderService    orderService;
    @Reference(version = "1.0.0")
    private SessionProvider sessionProvider;

    /**
     * 提交订单
     * 
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = ("/buyer/submitOrder"))
    public String submitOrder(Model model, Order order, HttpServletRequest request, HttpServletResponse response) {
        log.info("submitOrder start.");
        try {
            //设置request字符集
            //request.setCharacterEncoding("UTF-8");
            String username = sessionProvider.getAttributeForUser(RequestUtil.getCSESSIONID(request, response));
            //设置用户名
            order.setUsername(username);
            ResultBase<Integer> result = orderService.insertOrder(order);
            log.info("result:{}", ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        } catch (BizException e) {
            log.info("submitOrder biz error:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.info("submitOrder error:{}", e.getMessage(), e);
        }
        log.info("submitOrder end.");
        return "success";
    }
}
