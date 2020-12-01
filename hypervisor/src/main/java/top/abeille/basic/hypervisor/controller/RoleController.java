/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.service.RoleService;
import top.abeille.basic.hypervisor.vo.RoleVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 角色信息controller
 *
 * @author liwenqiang 2018/12/17 19:38
 **/
@RestController
@RequestMapping("/role")
public class RoleController extends AbstractController {

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
    public Flux<RoleVO> retrieveUser() {
        return roleService.retrieveAll();
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param roleDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RoleVO> createRole(@RequestBody @Valid RoleDTO roleDTO) {
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
    public Mono<RoleVO> modifyRole(@PathVariable String code, @RequestBody @Valid RoleDTO roleDTO) {
        return roleService.modify(code, roleDTO);
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<RoleVO> fetchGroup(String code) {
        return roleService.fetchByCode(code);
    }
}