/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.TranslationDTO;
import top.abeille.basic.assets.service.TranslationInfoService;
import top.abeille.basic.assets.vo.TranslationDetailsVO;
import top.abeille.basic.assets.vo.TranslationVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 翻译信息controller
 *
 * @author liwenqiang 2020/2/16 14:26
 **/
@RestController
@RequestMapping("/translation")
public class TranslationInfoController extends AbstractController {

    private final TranslationInfoService translationInfoService;

    public TranslationInfoController(TranslationInfoService translationInfoService) {
        this.translationInfoService = translationInfoService;
    }

    /**
     * 分页查询翻译信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<TranslationVO> retrieveTranslation() {
        Sort sort = super.initSortProperties();
        return translationInfoService.retrieveAll(sort);
    }

    /**
     * 根据传入的业务id: businessId 查询信息
     *
     * @param businessId 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{businessId}")
    public Mono<ResponseEntity<TranslationDetailsVO>> fetchTranslation(@PathVariable String businessId) {
        return translationInfoService.fetchDetailsByBusinessId(businessId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    /**
     * 根据传入的数据添加翻译信息
     *
     * @param translationDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<ResponseEntity<TranslationVO>> createTranslation(@RequestBody @Valid TranslationDTO translationDTO) {
        return translationInfoService.create(translationDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId     业务id
     * @param translationDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{businessId}")
    public Mono<ResponseEntity<TranslationVO>> modifyTranslation(@PathVariable String businessId, @RequestBody @Valid TranslationDTO translationDTO) {
        return translationInfoService.modify(businessId, translationDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_MODIFIED));
    }

}
