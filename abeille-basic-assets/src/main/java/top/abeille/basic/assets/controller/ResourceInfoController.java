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
import top.abeille.basic.assets.dto.ResourceDTO;
import top.abeille.basic.assets.service.ResourceInfoService;
import top.abeille.basic.assets.vo.ResourceVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 资源信息controller
 *
 * @author liwenqiang 2020/2/20 9:54
 **/
@RestController
@RequestMapping("/resource")
public class ResourceInfoController extends AbstractController {

    private final ResourceInfoService resourceInfoService;

    public ResourceInfoController(ResourceInfoService resourceInfoService) {
        this.resourceInfoService = resourceInfoService;
    }

    /**
     * 分页查询翻译信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<ResourceVO> retrieveArticle() {
        Sort sort = super.initSortProperties();
        return resourceInfoService.retrieveAll(sort);
    }

    /**
     * 根据传入的业务id: businessId 查询信息
     *
     * @param businessId 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{articleId}")
    public Mono<ResponseEntity<ResourceVO>> fetchArticle(@PathVariable String businessId) {
        return resourceInfoService.fetchById(businessId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param resourceDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<ResponseEntity<ResourceVO>> createArticle(@RequestBody @Valid ResourceDTO resourceDTO) {
        return resourceInfoService.create(resourceDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId  业务id
     * @param resourceDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{businessId}")
    public Mono<ResponseEntity<ResourceVO>> modifyArticle(@PathVariable String businessId, @RequestBody @Valid ResourceDTO resourceDTO) {
        return resourceInfoService.modify(businessId, resourceDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

}