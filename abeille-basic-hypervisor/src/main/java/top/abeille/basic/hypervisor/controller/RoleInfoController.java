/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.entity.RoleInfo;
import top.abeille.basic.hypervisor.service.RoleInfoService;
import top.abeille.common.basic.AbstractController;

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
    public Mono<RoleInfo> saveRole(@RequestBody RoleInfo role) {
        return roleInfoService.save(role);
    }

    /**
     * 编辑角色
     *
     * @param role 角色
     * @return ResponseEntity
     */
    @PutMapping
    public Mono<RoleInfo> modifyRole(@RequestBody RoleInfo role) {
        return roleInfoService.save(role);
    }

    /**
     * 删除角色——根据ID
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public Mono<Void> removeRole(@PathVariable Long id) {
        return roleInfoService.removeById(id);

    }

}
