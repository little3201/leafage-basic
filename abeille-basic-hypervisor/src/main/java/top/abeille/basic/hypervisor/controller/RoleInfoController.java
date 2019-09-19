/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.entity.RoleInfo;
import top.abeille.basic.hypervisor.service.RoleInfoService;
import top.abeille.common.basic.AbstractController;

import java.util.Objects;

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
     * 保存角色
     *
     * @param role 角色
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity saveRole(@RequestBody RoleInfo role) {
        Mono<RoleInfo> infoMono = roleInfoService.save(role);
        if (Objects.isNull(infoMono)) {
            log.error("Save role occurred error.");
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 编辑角色
     *
     * @param role 角色
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity modifyRole(@RequestBody RoleInfo role) {
        if (Objects.isNull(role.getId())) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        Mono<RoleInfo> infoMono = roleInfoService.save(role);
        if (Objects.isNull(infoMono)) {
            log.error("Modify role occurred error.");
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

}
