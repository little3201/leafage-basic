/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.service.GroupInfoService;
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
public class GroupInfoController extends AbstractController {

    private final GroupInfoService groupInfoService;

    public GroupInfoController(GroupInfoService groupInfoService) {
        this.groupInfoService = groupInfoService;
    }

    /**
     * 查找组信息
     *
     * @return ResponseEntity
     */
    @GetMapping
    public Flux<GroupVO> retrieveGroup() {
        Sort sort = super.initSortProperties();
        return groupInfoService.retrieveAll(sort);
    }

    /**
     * 添加组信息
     *
     * @param groupDTO 组
     * @return ResponseEntity
     */
    @PostMapping
    public Mono<ResponseEntity<GroupVO>> createGroup(@RequestBody @Valid GroupDTO groupDTO) {
        return groupInfoService.create(groupDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 修改组信息
     *
     * @param groupDTO 组
     * @return ResponseEntity
     */
    @PutMapping("/{groupId}")
    public Mono<ResponseEntity<GroupVO>> modifyGroup(@PathVariable Long groupId, @RequestBody @Valid GroupDTO groupDTO) {
        return groupInfoService.modify(groupId, groupDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 删除组信息
     *
     * @param groupId 业务主键
     * @return ResponseEntity
     */
    @DeleteMapping("/{groupId}")
    public Mono<ResponseEntity<Void>> removeGroup(@PathVariable Long groupId) {
        return groupInfoService.removeById(groupId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }
}
