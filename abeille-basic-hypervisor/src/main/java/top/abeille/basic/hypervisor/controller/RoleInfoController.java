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
     * 根据传入的数据添加信息
     *
     * @param roleDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<ResponseEntity<RoleVO>> createRole(@RequestBody @Valid RoleDTO roleDTO) {
        return roleInfoService.create(roleDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 根据传入的业务id: roleId 和要修改的数据，修改信息
     *
     * @param roleId  业务id
     * @param roleDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{roleId}")
    public Mono<ResponseEntity<RoleVO>> modifyRole(@PathVariable String roleId, @RequestBody @Valid RoleDTO roleDTO) {
        return roleInfoService.modify(roleId, roleDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

}
