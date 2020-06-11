package top.abeille.basic.hypervisor.handler;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AbeilleFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        if (exception instanceof UsernameNotFoundException) {
            return writeErrorMessage(response, "用户不存在");
        } else if (exception instanceof BadCredentialsException) {
            return writeErrorMessage(response, "密码错误");
        } else if (exception instanceof LockedException) {
            return writeErrorMessage(response, "账号锁定");
        }
        return writeErrorMessage(response, "服务异常");
    }

    private Mono<Void> writeErrorMessage(ServerHttpResponse response, String msg) {
        DataBuffer buffer = response.bufferFactory().wrap(msg.getBytes(UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
