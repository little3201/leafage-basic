package top.abeille.basic.authority.common.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * spring security 配置——登录成功处理器
 *
 * @author liwenqiang 2018/11/27 16:51
 **/
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private final ObjectMapper objectMapper;

    @Autowired
    public LoginSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        String userAgent = request.getHeader("user-agent");
        if (StringUtils.startsWithIgnoreCase(userAgent, "Mozilla/5.0")) {
            String url = null;
            SavedRequest savedRequest = requestCache.getRequest(request, response);
            if (savedRequest != null) {
                url = savedRequest.getRedirectUrl();
            }
            //默认情况下跳转至"/"路径
            if (StringUtils.isBlank(url)) {
                getRedirectStrategy().sendRedirect(request, response, "/");
            }
            super.onAuthenticationSuccess(request, response, authentication);
        } else {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResponseEntity.ok(authentication.getDetails())));
        }
    }
}
