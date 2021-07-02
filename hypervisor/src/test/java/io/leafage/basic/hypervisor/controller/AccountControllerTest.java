package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.service.AccountService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;

/**
 * account接口测试类
 *
 * @author liwenqiang 2021/6/19 10:00
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountService accountService;

    @Test
    void fetch() {
        AccountVO accountVO = new AccountVO();
        accountVO.setBalance(new BigDecimal("1000.09"));
        given(this.accountService.fetch(Mockito.anyString())).willReturn(Mono.just(accountVO));

        webTestClient.get().uri("/account/{code}", "21612OL34").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.balance").isEqualTo("1000.09");
    }

    @Test
    void create() {
        AccountVO accountVO = new AccountVO();
        accountVO.setBalance(new BigDecimal("1000.09"));
        given(this.accountService.create(Mockito.any(AccountDTO.class))).willReturn(Mono.just(accountVO));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(new BigDecimal("1000.09"));
        webTestClient.post().uri("/account").bodyValue(accountDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.balance").isEqualTo("1000.09");
    }

    @Test
    void modify() {
        AccountVO accountVO = new AccountVO();
        accountVO.setBalance(new BigDecimal("1000.09"));
        given(this.accountService.modify(Mockito.anyString(), Mockito.any(AccountDTO.class))).willReturn(Mono.just(accountVO));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(new BigDecimal("1000.09"));
        webTestClient.put().uri("/account/{code}", "21612OL34").bodyValue(accountDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.balance").isEqualTo("1000.09");
    }

    @Test
    void remove() {
        given(this.accountService.remove(Mockito.anyString())).willReturn(Mono.empty());

        webTestClient.delete().uri("/account/{code}", "21612OL34").exchange()
                .expectStatus().isOk();
    }
}