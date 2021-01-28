/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.CountVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Set;

/**
 * 角色信息controller
 *
 * @author liwenqiang 2018/12/17 19:38
 **/
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 查询角色信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<RoleVO> retrieve(@RequestParam int page, @RequestParam int size) {
        return roleService.retrieve(page, size);
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<RoleVO> fetch(@PathVariable String code) {
        return roleService.fetch(code);
    }

    /**
     * 统计关联信息
     *
     * @param codes code集合
     * @return 统计信息
     */
    @GetMapping("/count")
    public Flux<CountVO> countUsers(@RequestParam Set<String> codes) {
        return roleService.countUsers(codes);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param roleDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RoleVO> create(@RequestBody @Valid RoleDTO roleDTO) {
        return roleService.create(roleDTO);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code    代码
     * @param roleDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<RoleVO> modify(@PathVariable String code, @RequestBody @Valid RoleDTO roleDTO) {
        return roleService.modify(code, roleDTO);
    }

}
