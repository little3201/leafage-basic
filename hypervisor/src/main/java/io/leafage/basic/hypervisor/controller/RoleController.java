/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * 角色信息controller
 *
 * @author liwenqiang 2018/12/17 19:38
 **/
@RestController
@RequestMapping("/role")
public class RoleController {

    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

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
    public ResponseEntity<Flux<RoleVO>> retrieve(Integer page, Integer size) {
        Flux<RoleVO> voFlux;
        try {
            if (page == null || size == null) {
                voFlux = roleService.retrieve();
            } else {
                voFlux = roleService.retrieve(page, size);
            }
        } catch (Exception e) {
            logger.error("Retrieve role occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<Mono<RoleVO>> fetch(@PathVariable String code) {
        Mono<RoleVO> voMono;
        try {
            voMono = roleService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch role occurred an error: ", e);
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
            count = roleService.count();
        } catch (Exception e) {
            logger.error("Count role occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param roleDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Mono<RoleVO>> create(@RequestBody @Valid RoleDTO roleDTO) {
        Mono<RoleVO> voMono;
        try {
            voMono = roleService.create(roleDTO);
        } catch (Exception e) {
            logger.error("Create role occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code    代码
     * @param roleDTO 要修改的数据
     * @return 修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<Mono<RoleVO>> modify(@PathVariable String code, @RequestBody @Valid RoleDTO roleDTO) {
        Mono<RoleVO> voMono;
        try {
            voMono = roleService.modify(code, roleDTO);
        } catch (Exception e) {
            logger.error("Modify role occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(voMono);
    }

}
