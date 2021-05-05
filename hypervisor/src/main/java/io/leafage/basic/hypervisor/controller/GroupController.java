/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * 分组信息controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/group")
public class GroupController {

    private final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * 查询组信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 根据传入的业务id: code 查询信息
     *
     * @param code 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<Mono<GroupVO>> fetch(@PathVariable String code) {
        Mono<GroupVO> voMono;
        try {
            voMono = groupService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch group occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 统计记录数
     *
     * @return 记录数
     */
    @GetMapping("/count")
    public ResponseEntity<Mono<Long>> count() {
        Mono<Long> count;
        try {
            count = groupService.count();
        } catch (Exception e) {
            logger.error("Count group occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 根据传入的数据添加信息
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
     * 根据传入的代码和要修改的数据，修改信息
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
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(voMono);
    }

    /**
     * 根据传入的代码删除信息（逻辑删除）
     *
     * @param code 代码
     * @return 200状态码，异常时返回417状态码
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Mono<Void>> remove(@PathVariable String code) {
        try {
            groupService.remove(code);
        } catch (Exception e) {
            logger.error("Remove group occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }

}
