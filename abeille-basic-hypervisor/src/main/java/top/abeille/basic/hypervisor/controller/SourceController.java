/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.SourceDTO;
import top.abeille.basic.hypervisor.service.SourceService;
import top.abeille.basic.hypervisor.vo.SourceVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 权限资源controller
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
@RestController
@RequestMapping("/source")
public class SourceController extends AbstractController {

    private final SourceService sourceService;

    public SourceController(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    /**
     * 查询资源信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<SourceVO> retrieveSource() {
        return sourceService.retrieveAll();
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param sourceDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<SourceVO> createSource(@RequestBody @Valid SourceDTO sourceDTO) {
        return sourceService.create(sourceDTO);
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId 业务id
     * @param sourceDTO  要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PostMapping("{businessId}")
    public Mono<SourceVO> modifySource(@PathVariable String businessId, @RequestBody @Valid SourceDTO sourceDTO) {
        return sourceService.modify(businessId, sourceDTO);
    }

    /**
     * 根据传入的业务id: businessId 查询信息
     *
     * @param businessId 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{businessId}")
    public Mono<SourceVO> fetchGroup(String businessId) {
        return sourceService.fetchByBusinessId(businessId);
    }
}
