package top.abeille.basic.profile.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.abeille.basic.profile.api.vo.UserInfoVO;

/**
 * 用户api
 *
 * @author liwenqiang 2019-03-03 22:55
 **/
@FeignClient(name = "abeille-basic-authority")
public interface UserApi {

    @GetMapping("/v1/user")
    UserInfoVO getUserInfo(@RequestParam("id") Long id);
}