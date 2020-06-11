/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.service.GroupService;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 组信息controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/group")
public class GroupController extends AbstractController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * 分页查询翻译信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Mono<ServerResponse> retrieveGroup() {
        return ServerResponse.ok().body(BodyInserters.fromValue(groupService.retrieveAll()))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build());
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param groupDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<ServerResponse> createGroup(@RequestBody @Valid GroupDTO groupDTO) {
        return ServerResponse.ok().body(BodyInserters.fromValue(groupService.create(groupDTO)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.EXPECTATION_FAILED).build());
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId 业务id
     * @param groupDTO   要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{businessId}")
    public Mono<ServerResponse> modifyGroup(@PathVariable String businessId, @RequestBody @Valid GroupDTO groupDTO) {
        return ServerResponse.ok().body(BodyInserters.fromValue(groupService.modify(businessId, groupDTO)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_MODIFIED).build());
    }

    /**
     * 根据传入的业务id: businessId 删除信息（逻辑删除）
     *
     * @param businessId 业务id
     * @return 如果删除数据成功，返回删除后的信息，否则返回417状态码
     */
    @DeleteMapping("/{businessId}")
    public Mono<ServerResponse> removeGroup(@PathVariable String businessId) {
        return ServerResponse.ok().body(BodyInserters.fromValue(groupService.removeById(businessId)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.EXPECTATION_FAILED).build());
    }
}
