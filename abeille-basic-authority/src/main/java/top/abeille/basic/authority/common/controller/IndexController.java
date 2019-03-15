package top.abeille.basic.authority.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录成功页
 *
 * @author liwenqiang 2018/12/17 9:19
 **/
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "/index.html";
    }
}
