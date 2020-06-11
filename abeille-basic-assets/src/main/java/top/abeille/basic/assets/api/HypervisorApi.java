/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.api.bo.UserBO;

/**
 * 用户api
 *
 * @author liwenqiang 2019-03-03 22:55
 **/
@FeignClient(name = "abeille-basic-hypervisor")
public interface HypervisorApi {

    /**
     * 根据传入的业务id: businessId 查询信息
     *
     * @param businessId 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/user/{businessId}")
    Mono<ResponseEntity<UserBO>> fetchUserByBusinessId(@PathVariable("businessId") String businessId);
}
