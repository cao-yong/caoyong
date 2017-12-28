package com.caoyong.conf;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.core.bean.system.User;
import com.caoyong.core.service.system.UserService;
import com.caoyong.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
/**
 * 登录成功后处理器
 *
 * @author yong.cao
 * @since 2017年12月28日上午10:35:02
 */
@Slf4j
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Reference(version = "1.0.0", timeout = 3000000)
    private UserService userService;

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            MyUserDetail userDetail = (MyUserDetail) authentication.getPrincipal();
            User currentUser = new User();
            currentUser.setId(userDetail.getUserId());
            currentUser.setUsername(userDetail.getUsername());
            currentUser.setOperateDate(new Date());
            currentUser.setRoles(userDetail.getRoleList());
            currentUser.setName(userDetail.getName());
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", currentUser);
            if (response.isCommitted()) {
                log.info("Can't redirect");
                return;
            }
            //更新用户最后登录时间
            userService.updateUserOperateDateByUserId(currentUser.getId());
            redirectStrategy.sendRedirect(request, response, "/control/index.do");
        } catch (BizException e) {
            log.error("CustomSuccessHandler biz error:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("CustomSuccessHandler error:{}", e.getMessage(), e);
        }
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

}