/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.TranslationDTO;
import top.abeille.basic.assets.service.TranslationService;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 翻译信息controller
 *
 * @author liwenqiang 2020/2/16 14:26
 **/
@RestController
@RequestMapping("/translation")
public class TranslationController extends AbstractController {

    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    /**
     * 分页查询翻译信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Mono<ServerResponse> retrieveTranslation() {
        return ServerResponse.ok().body(BodyInserters.fromValue(translationService.retrieveAll()))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build());
    }

    /**
     * 根据传入的业务id: businessId 查询信息
     *
     * @param businessId 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{businessId}")
    public Mono<ServerResponse> fetchTranslation(@PathVariable String businessId) {
        return ServerResponse.ok().body(BodyInserters.fromValue(translationService.fetchDetailsByBusinessId(businessId)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build());
    }

    /**
     * 根据传入的数据添加翻译信息
     *
     * @param translationDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<ServerResponse> createTranslation(@RequestBody @Valid TranslationDTO translationDTO) {
        return ServerResponse.ok().body(BodyInserters.fromValue(translationService.create(translationDTO)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.EXPECTATION_FAILED).build());
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId     业务id
     * @param translationDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{businessId}")
    public Mono<ServerResponse> modifyTranslation(@PathVariable String businessId, @RequestBody @Valid TranslationDTO translationDTO) {
        return ServerResponse.ok().body(BodyInserters.fromValue(translationService.modify(businessId, translationDTO)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_MODIFIED).build());
    }

}
