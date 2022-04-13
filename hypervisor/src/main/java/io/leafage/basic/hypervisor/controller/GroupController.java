/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.service.AccountGroupService;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import javax.validation.Valid;

/**
 * group controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/group")
public class GroupController {

    private final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final AccountGroupService accountGroupService;
    private final GroupService groupService;

    public GroupController(AccountGroupService accountGroupService, GroupService groupService) {
        this.accountGroupService = accountGroupService;
        this.groupService = groupService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Flux<GroupVO>> retrieve(Integer page, Integer size) {
        Flux<GroupVO> voFlux;
        try {
            if (page == null || size == null) {
                voFlux = groupService.retrieve();
            } else {
                voFlux = groupService.retrieve(page, size);
            }
        } catch (Exception e) {
            logger.error("Retrieve group occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 树形查询
     *
     * @return 查询到的数据，否则返回空
     */
    @GetMapping("/tree")
    public ResponseEntity<Flux<TreeNode>> tree() {
        Flux<TreeNode> authorities;
        try {
            authorities = groupService.tree();
        } catch (Exception e) {
            logger.info("Retrieve group occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authorities);
    }

    /**
     * 根据 code 查询
     *
     * @param code 业务id
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<Mono<GroupVO>> fetch(@PathVariable String code) {
        Mono<GroupVO> voMono;
        try {
            voMono = groupService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch group occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 统计记录数
     *
     * @return 查询的记录数，异常时返回204状态码
     */
    @GetMapping("/count")
    public ResponseEntity<Mono<Long>> count() {
        Mono<Long> count;
        try {
            count = groupService.count();
        } catch (Exception e) {
            logger.error("Count group occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 是否已存在
     *
     * @param name 用户名
     * @return true-是，false-否
     */
    @GetMapping("/exist")
    public ResponseEntity<Mono<Boolean>> exist(@RequestParam String name) {
        Mono<Boolean> existsMono;
        try {
            existsMono = groupService.exist(name);
        } catch (Exception e) {
            logger.error("Check group is exist an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(existsMono);
    }

    /**
     * 添加信息
     *
     * @param groupDTO 要添加的数据
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<GroupVO>> create(@RequestBody @Valid GroupDTO groupDTO) {
        Mono<GroupVO> voMono;
        try {
            voMono = groupService.create(groupDTO);
        } catch (Exception e) {
            logger.error("Create group occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 修改信息
     *
     * @param code     代码
     * @param groupDTO 要修改的数据
     * @return 修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<Mono<GroupVO>> modify(@PathVariable String code, @RequestBody @Valid GroupDTO groupDTO) {
        Mono<GroupVO> voMono;
        try {
            voMono = groupService.modify(code, groupDTO);
        } catch (Exception e) {
            logger.error("Modify group occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

    /**
     * 删除信息（逻辑删除）
     *
     * @param code 代码
     * @return 200状态码，异常时返回417状态码
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Mono<Void>> remove(@PathVariable String code) {
        Mono<Void> voidMono;
        try {
            voidMono = groupService.remove(code);
        } catch (Exception e) {
            logger.error("Remove group occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok(voidMono);
    }

    /**
     * 查询关联用户
     *
     * @param code 组code
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{code}/account")
    public ResponseEntity<Flux<AccountVO>> accounts(@PathVariable String code) {
        Flux<AccountVO> voFlux;
        try {
            voFlux = accountGroupService.accounts(code);
        } catch (Exception e) {
            logger.error("Retrieve group accounts occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

}
