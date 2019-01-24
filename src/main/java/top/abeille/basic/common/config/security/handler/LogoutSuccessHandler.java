package top.abeille.basic.common.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 退出成功处理器
 *
 * @author liwenqiang 2018/12/20 17:57
 **/
@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        //清空session
        HttpSession session = request.getSession();
        session.invalidate();
        String userAgent = request.getHeader("user-agent");
        if (StringUtils.startsWithIgnoreCase(userAgent, "Mozilla/5.0")) {
            super.onLogoutSuccess(request, response, authentication);
        } else {
            //输出结果
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResponseEntity.ok()));
        }
    }
}
