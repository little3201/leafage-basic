/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.service.RoleService;
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
     * 根据传入的数据添加信息
     *
     * @param roleDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<ServerResponse> createRole(@RequestBody @Valid RoleDTO roleDTO) {
        return ServerResponse.ok().body(BodyInserters.fromValue(roleService.create(roleDTO)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.EXPECTATION_FAILED).build());
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId 业务id
     * @param roleDTO    要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{businessId}")
    public Mono<ServerResponse> modifyRole(@PathVariable String businessId, @RequestBody @Valid RoleDTO roleDTO) {
        return ServerResponse.ok().body(BodyInserters.fromValue(roleService.modify(businessId, roleDTO)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_MODIFIED).build());
    }

}
