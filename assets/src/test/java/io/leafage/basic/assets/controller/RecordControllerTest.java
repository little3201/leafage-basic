package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.RecordDTO;
import io.leafage.basic.assets.service.RecordService;
import io.leafage.basic.assets.vo.CategoryVO;
import io.leafage.basic.assets.vo.RecordVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.given;

/**
 * record controller test
 *
 * @author liwenqiang 2022/3/18 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(RecordController.class)
class RecordControllerTest {

    @MockBean
    private RecordService recordService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        RecordVO recordVO = new RecordVO();
        recordVO.setType("bugfix");
        given(this.recordService.retrieve(Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(Flux.just(recordVO));

        webTestClient.get().uri("/record").exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.recordService.retrieve())
                .willThrow(new NoSuchElementException());

        webTestClient.get().uri("/record").exchange().expectStatus().isNoContent();
    }

    @Test
    void count() {
        given(this.recordService.count()).willReturn(Mono.just(2L));
        webTestClient.get().uri("/record/count").exchange().expectStatus().isOk();
    }

    @Test
    void count_error() {
        given(this.recordService.count()).willThrow(new RuntimeException());
        webTestClient.get().uri("/record/count").exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        // 构造返回对象
        RecordVO recordVO = new RecordVO();
        recordVO.setDate(LocalDate.now());
        recordVO.setType("bugfix");
        given(this.recordService.create(Mockito.any(RecordDTO.class))).willReturn(Mono.just(recordVO));

        // 构造请求对象
        RecordDTO recordDTO = new RecordDTO();
        recordDTO.setDate(LocalDate.now());
        recordDTO.setType("bugfix");
        recordDTO.setItems(List.of("更新依赖"));
        webTestClient.post().uri("/record").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(recordDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.type").isNotEmpty();
    }

    @Test
    void create_error() {
        given(this.recordService.create(Mockito.any(RecordDTO.class))).willThrow(new NoSuchElementException());

        // 构造请求对象
        RecordDTO recordDTO = new RecordDTO();
        recordDTO.setDate(LocalDate.now());
        recordDTO.setType("bugfix");
        recordDTO.setItems(List.of("更新依赖"));
        webTestClient.post().uri("/record").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(recordDTO).exchange()
                .expectStatus().is4xxClientError();
    }
}