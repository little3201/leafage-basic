package top.abeille.basic.authority.common.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 *
 * @author liwenqiang 2018/12/20 17:56
 **/
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Autowired
    public LoginFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String userAgent = request.getHeader("user-agent");
        if (StringUtils.startsWithIgnoreCase(userAgent, "Mozilla/5.0")) {
            super.setDefaultFailureUrl("/login?error");
            super.onAuthenticationFailure(request, response, exception);
        } else {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value())));
        }
    }
}
