package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.NotificationDTO;
import io.leafage.basic.hypervisor.service.NotificationService;
import io.leafage.basic.hypervisor.vo.NotificationVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;

/**
 * notification controller test
 *
 * @author liwenqiang 2022/2/16 9:03
 **/
@ExtendWith(SpringExtension.class)
@WebFluxTest(NotificationController.class)
class NotificationControllerTest {

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private WebTestClient webTestClient;


    @Test
    void retrieve() {
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle("标题");
        notificationVO.setContent("内容");
        notificationVO.setReceiver("test");
        notificationVO.setModifyTime(LocalDateTime.now());
        given(this.notificationService.retrieve(0, 2, false)).willReturn(Flux.just(notificationVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/notification").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("read", "false").build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.notificationService.retrieve(0, 2, false)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/notification").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("read", "false").build())
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle("标题");
        given(this.notificationService.fetch(Mockito.anyString())).willReturn(Mono.just(notificationVO));

        webTestClient.get().uri("/notification/{code}", "1100").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.title").isEqualTo("标题");
    }

    @Test
    void fetch_error() {
        given(this.notificationService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/notification/{code}", "1100").exchange().expectStatus().isNoContent();
    }


    @Test
    void count() {
        given(this.notificationService.count()).willReturn(Mono.just(2L));

        webTestClient.get().uri("/notification/count").exchange().expectStatus().isOk();
    }

    @Test
    void count_error() {
        given(this.notificationService.count()).willThrow(new RuntimeException());

        webTestClient.get().uri("/notification/count").exchange().expectStatus().isNoContent();
    }


    @Test
    void create() {
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle("标题");
        given(this.notificationService.create(Mockito.any(NotificationDTO.class))).willReturn(Mono.just(notificationVO));

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("标题");
        notificationDTO.setContent("内容信息");
        notificationDTO.setReceiver("test");
        webTestClient.post().uri("/notification").bodyValue(notificationDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isEqualTo("标题");
    }

    @Test
    void create_error() {
        given(this.notificationService.create(Mockito.any(NotificationDTO.class))).willThrow(new RuntimeException());

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("标题");
        notificationDTO.setContent("内容信息");
        notificationDTO.setReceiver("test");
        webTestClient.post().uri("/notification").bodyValue(notificationDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void remove() {
        given(this.notificationService.remove(Mockito.anyString())).willReturn(Mono.empty());

        webTestClient.delete().uri("/notification/{code}", "21612OL34").exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.notificationService.remove(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.delete().uri("/notification/{code}", "21612OL34").exchange()
                .expectStatus().is4xxClientError();
    }
}