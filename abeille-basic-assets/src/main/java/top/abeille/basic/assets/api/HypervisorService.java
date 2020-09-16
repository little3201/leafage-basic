package top.abeille.basic.assets.api;

import org.apache.http.util.Asserts;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.bo.UserBO;

@Service
public class HypervisorService implements HypervisorApi {

    private final WebClient.Builder clientBuilder;

    public HypervisorService(WebClient.Builder clientBuilder) {
        clientBuilder.baseUrl("http://abeille-basic-hypervisor").build();
        this.clientBuilder = clientBuilder;
    }

    @Override
    public Mono<UserBO> fetchUserByBusinessId(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        return clientBuilder.build().get().uri("/user/{businessId}", businessId)
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(UserBO.class);
    }
}
