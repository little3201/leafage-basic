package top.abeille.basic.hypervisor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AbeilleSuccessHandler implements ServerAuthenticationSuccessHandler {

    protected static final Logger log = LoggerFactory.getLogger(AbeilleSuccessHandler.class);

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String result = "{\"isAuth\":" + authentication.isAuthenticated() + "}";
        DataBuffer buffer = response.bufferFactory().wrap(result.getBytes(UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
