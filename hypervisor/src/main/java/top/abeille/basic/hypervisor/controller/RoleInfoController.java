/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.service.RoleService;
import top.abeille.basic.hypervisor.vo.RoleVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 角色信息接口
 *
 * @author liwenqiang 2018/12/17 19:38
 **/
@RestController
@RequestMapping("/role")
public class RoleInfoController extends AbstractController {

    private final RoleService roleService;

    public RoleInfoController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 分页查询翻译信息
     *
     * @param pageNum  当前页
     * @param pageSize 页内数据量
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public ResponseEntity<Object> retrieveRole(Integer pageNum, Integer pageSize) {
        Pageable pageable = super.initPageParams(pageNum, pageSize);
        Page<RoleVO> roles = roleService.retrieveByPage(pageable);
        if (CollectionUtils.isEmpty(roles.getContent())) {
            logger.info("Not found anything about role with pageable.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param roleDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<Object> createRole(@RequestBody @Valid RoleDTO roleDTO) {
        try {
            roleService.create(roleDTO);
        } catch (Exception e) {
            logger.error("Save role occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId 业务id
     * @param roleDTO    要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{businessId}")
    public ResponseEntity<Object> modifyRole(@PathVariable String businessId, @RequestBody @Valid RoleDTO roleDTO) {
        try {
            roleService.modify(businessId, roleDTO);
        } catch (Exception e) {
            logger.error("Modify role occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 根据传入的业务id: businessId 删除信息
     *
     * @param businessId 业务id
     * @return 如果删除成功，返回200状态码，否则返回417状态码
     */
    @DeleteMapping("/{businessId}")
    public ResponseEntity<Object> removeRole(@PathVariable String businessId) {
        try {
            roleService.removeById(businessId);
        } catch (Exception e) {
            logger.error("Remove role occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
