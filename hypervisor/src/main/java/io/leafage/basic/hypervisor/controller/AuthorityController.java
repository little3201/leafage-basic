/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * 权限 controller
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
@RestController
@RequestMapping("/authority")
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * 查询资源信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<AuthorityVO> retrieve(Integer page, Integer size, String type) {
        if (page != null && size != null) {
            return authorityService.retrieve(page, size);
        } else {
            return authorityService.retrieve(type);
        }
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<AuthorityVO> fetch(@PathVariable String code) {
        return authorityService.fetch(code);
    }

    /**
     * 统计记录数
     *
     * @return 记录数
     */
    @GetMapping("/count")
    public Mono<Long> count() {
        return authorityService.count();
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param authorityDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthorityVO> create(@RequestBody @Valid AuthorityDTO authorityDTO) {
        return authorityService.create(authorityDTO);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code         代码
     * @param authorityDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<AuthorityVO> modify(@PathVariable String code, @RequestBody @Valid AuthorityDTO authorityDTO) {
        return authorityService.modify(code, authorityDTO);
    }

}
