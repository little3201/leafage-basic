package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.service.AccountGroupService;
import io.leafage.basic.hypervisor.service.AccountRoleService;
import io.leafage.basic.hypervisor.service.AccountService;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
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
    private AccountGroupService accountGroupService;

    @MockBean
    private AccountRoleService accountRoleService;

    @MockBean
    private AuthorityService authorityService;

    @MockBean
    private AccountService accountService;

    @Test
    void retrieve() {
        AccountVO accountVO = new AccountVO();
        accountVO.setUsername("little3201");
        accountVO.setNickname("test");
        Page<AccountVO> voPage = new PageImpl<>(List.of(accountVO));
        given(this.accountService.retrieve(0, 2)).willReturn(Mono.just(voPage));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/account").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(AccountVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.accountService.retrieve(0, 2)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/account").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        AccountVO accountVO = new AccountVO();
        accountVO.setUsername("leafage");
        accountVO.setAvatar("/avatar.jpg");
        given(this.accountService.fetch(Mockito.anyString())).willReturn(Mono.just(accountVO));

        webTestClient.get().uri("/account/{username}", "little3201").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.username").isEqualTo("leafage");
    }

    @Test
    void fetch_error() {
        given(this.accountService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/account/{username}", "little3201").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void create() {
        AccountVO accountVO = new AccountVO();
        accountVO.setUsername("leafage");
        accountVO.setAvatar("/avatar.jpg");
        given(this.accountService.create(Mockito.any(AccountDTO.class))).willReturn(Mono.just(accountVO));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername("leafage");
        webTestClient.post().uri("/account").bodyValue(accountDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.username").isEqualTo("leafage");
    }

    @Test
    void create_error() {
        given(this.accountService.create(Mockito.any(AccountDTO.class))).willThrow(new RuntimeException());

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername("leafage");
        webTestClient.post().uri("/account").bodyValue(accountDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        AccountVO accountVO = new AccountVO();
        accountVO.setUsername("leafage");
        accountVO.setAvatar("/avatar.jpg");
        given(this.accountService.modify(Mockito.anyString(), Mockito.any(AccountDTO.class))).willReturn(Mono.just(accountVO));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername("leafage");
        webTestClient.put().uri("/account/{username}", "little3201").bodyValue(accountDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.username").isEqualTo("leafage");
    }

    @Test
    void modify_error() {
        given(this.accountService.modify(Mockito.anyString(), Mockito.any(AccountDTO.class))).willThrow(new RuntimeException());

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername("leafage");
        webTestClient.put().uri("/account/{username}", "little3201").bodyValue(accountDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void unlock() {
        given(this.accountService.unlock(Mockito.anyString())).willReturn(Mono.empty());

        webTestClient.patch().uri("/account/{username}", "little3201").exchange()
                .expectStatus().isAccepted();
    }

    @Test
    void unlock_error() {
        given(this.accountService.unlock(Mockito.anyString())).willThrow(new NoSuchElementException());

        webTestClient.patch().uri("/account/{username}", "little3201").exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void remove() {
        given(this.accountService.remove(Mockito.anyString())).willReturn(Mono.empty());

        webTestClient.delete().uri("/account/{username}", "little3201").exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.accountService.remove(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.delete().uri("/account/{username}", "little3201").exchange()
                .expectStatus().is4xxClientError();
    }


    @Test
    void groups() {
        given(this.accountGroupService.groups(Mockito.anyString())).willReturn(Mono.just(List.of("21612OL34")));

        webTestClient.get().uri("/account/{username}/group", "little3201").exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class);
    }

    @Test
    void groups_error() {
        given(this.accountGroupService.groups(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/account/{username}/group", "little3201").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void group() {
        given(this.accountGroupService.relation(Mockito.anyString(), Mockito.anySet())).willReturn(Mono.just(true));

        webTestClient.patch().uri("/account/{username}/group", "little3201")
                .bodyValue(Collections.singleton("21612OL34"))
                .exchange().expectStatus().isAccepted();
    }

    @Test
    void group_error() {
        given(this.accountGroupService.relation(Mockito.anyString(), Mockito.anySet())).willThrow(new RuntimeException());

        webTestClient.patch().uri("/account/{username}/group", "little3201")
                .bodyValue(Collections.singleton("21612OL34"))
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    void roles() {
        given(this.accountRoleService.roles(Mockito.anyString())).willReturn(Mono.just(List.of("21612OL34")));

        webTestClient.get().uri("/account/{username}/role", "little3201").exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class);
    }

    @Test
    void roles_error() {
        given(this.accountRoleService.roles(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/account/{username}/role", "little3201").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void role() {
        given(this.accountRoleService.relation(Mockito.anyString(), Mockito.anySet())).willReturn(Mono.just(true));

        webTestClient.patch().uri("/account/{username}/role", "little3201")
                .bodyValue(Collections.singleton("21612OL34"))
                .exchange().expectStatus().isAccepted();
    }

    @Test
    void role_error() {
        given(this.accountRoleService.relation(Mockito.anyString(), Mockito.anySet())).willThrow(new RuntimeException());

        webTestClient.patch().uri("/account/{username}/role", "little3201")
                .bodyValue(Collections.singleton("21612OL34"))
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    void authority() {
        TreeNode treeNode = new TreeNode("21612OL31", "Dashboard");
        given(this.authorityService.authorities(Mockito.anyString())).willReturn(Flux.just(treeNode));

        webTestClient.get().uri("/account/{username}/authority", "little3201").exchange()
                .expectStatus().isOk()
                .expectBodyList(RoleVO.class);
    }

    @Test
    void authority_error() {
        given(this.authorityService.authorities(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/account/{username}/authority", "little3201").exchange()
                .expectStatus().isNoContent();
    }
}