package top.abeille.basic.authority.common.controller;


import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import top.abeille.common.basic.BasicController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LoginController-登陆
 *
 * @author liwenqiang 2018/7/9 9:54
 **/
@RestController
public class SignController extends BasicController {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 登录入口
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/login")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity login(HttpServletRequest request, HttpServletResponse response) {
        String userAgent = request.getHeader("user-agent");
        //请求中如果包含"/remote"则返回json数据，否则跳转至默认的登录页
        if (StringUtils.startsWithIgnoreCase(userAgent, "Mozilla/5.0")) {
            try {
                /* 登录的页面如果是.html需在resources 目录下，spring boot优先取resources下对应的页面
                    如果是.ftl等模板文件，需要配置路径解析器(即添加url映射)，否则无法访问 */
                redirectStrategy.sendRedirect(request, response, "/login.html");
            } catch (IOException e) {
                log.error("redirect to the url={'/login.html'} occurred error. e={}", e);
            }
        }
        return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

}

