/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.api;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.api.bo.UserBO;

@Service
public class HypervisorApiImpl implements HypervisorApi {

    private final WebClient client = WebClient.builder().baseUrl("http://abeille-basic-hypervisor").build();

    @Override
    public Mono<UserBO> fetchUserByBusinessId(String businessId) {
        return client.get().uri("/user/{businessId}", businessId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(UserBO.class);
    }
}
