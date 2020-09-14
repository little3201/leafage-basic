/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.api;

import org.apache.http.util.Asserts;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.bo.UserBO;

/**
 * 用户api
 *
 * @author liwenqiang 2019-03-03 22:55
 **/
public interface HypervisorApi {

    /**
     * 根据传入的业务id: businessId 查询信息
     *
     * @param businessId 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    default Mono<UserBO> fetchUserByBusinessId(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        return WebClient.create("http://abeille-basic-hypervisor").get().uri("/user/{businessId}", businessId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(UserBO.class);
    }
}
