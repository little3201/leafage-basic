/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.service.RoleInfoService;
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
public class RoleInfoController extends AbstractController {

    private final RoleInfoService roleInfoService;

    public RoleInfoController(RoleInfoService roleInfoService) {
        this.roleInfoService = roleInfoService;
    }

    /**
     * 添加角色信息
     *
     * @param roleDTO 角色
     * @return ResponseEntity
     */
    @PostMapping
    public Mono<ResponseEntity<RoleVO>> createRole(@RequestBody @Valid RoleDTO roleDTO) {
        return roleInfoService.create(roleDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 编辑角色信息
     *
     * @param roleDTO 角色
     * @return ResponseEntity
     */
    @PutMapping("/{roleId}")
    public Mono<ResponseEntity<RoleVO>> modifyRole(@PathVariable Long roleId, @RequestBody @Valid RoleDTO roleDTO) {
        return roleInfoService.modify(roleId, roleDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

}
