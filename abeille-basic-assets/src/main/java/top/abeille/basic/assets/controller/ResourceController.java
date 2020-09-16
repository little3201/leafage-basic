/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.ResourceDTO;
import top.abeille.basic.assets.service.ResourceService;
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
public class ResourceController extends AbstractController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * 分页查询信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<ResourceVO> retrieveResource() {
        return resourceService.retrieveAll();
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<ResourceVO> fetchResource(@PathVariable String code) {
        return resourceService.fetchByCode(code);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param resourceDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<ResourceVO> createResource(@RequestBody @Valid ResourceDTO resourceDTO) {
        return resourceService.create(resourceDTO);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code        代码
     * @param resourceDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    public Mono<ResourceVO> modifyResource(@PathVariable String code, @RequestBody @Valid ResourceDTO resourceDTO) {
        return resourceService.modify(code, resourceDTO);
    }

}
