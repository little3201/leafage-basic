/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.service.GroupService;
import top.abeille.basic.hypervisor.vo.GroupVO;
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
    public Flux<GroupVO> retrieveGroup() {
        return groupService.retrieveAll();
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param groupDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<ResponseEntity<GroupVO>> createGroup(@RequestBody @Valid GroupDTO groupDTO) {
        return groupService.create(groupDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId 业务id
     * @param groupDTO   要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{businessId}")
    public Mono<ResponseEntity<GroupVO>> modifyGroup(@PathVariable String businessId, @RequestBody @Valid GroupDTO groupDTO) {
        return groupService.modify(businessId, groupDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_MODIFIED));
    }

    /**
     * 根据传入的业务id: businessId 删除信息（逻辑删除）
     *
     * @param businessId 业务id
     * @return 如果删除数据成功，返回删除后的信息，否则返回417状态码
     */
    @DeleteMapping("/{businessId}")
    public Mono<ResponseEntity<Void>> removeGroup(@PathVariable String businessId) {
        return groupService.removeById(businessId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }
}
