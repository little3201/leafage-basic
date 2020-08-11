package top.abeille.basic.hypervisor.handler;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
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
            response.setStatusCode(HttpStatus.NO_CONTENT);
            return writeErrorMessage(response, "User Not Found");
        } else if (exception instanceof BadCredentialsException) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return writeErrorMessage(response, "Bad Credentials");
        } else if (exception instanceof LockedException) {
            response.setStatusCode(HttpStatus.LOCKED);
            return writeErrorMessage(response, "User Locked");
        }
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return writeErrorMessage(response, "Service Unavailable");
    }

    private Mono<Void> writeErrorMessage(ServerHttpResponse response, String msg) {
        DataBuffer buffer = response.bufferFactory().wrap(msg.getBytes(UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
