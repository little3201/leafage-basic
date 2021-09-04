/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.entity.UserGroup;
import io.leafage.basic.hypervisor.entity.UserRole;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.service.UserGroupService;
import io.leafage.basic.hypervisor.service.UserRoleService;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.leafage.common.basic.TreeNode;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * 用户信息接口.
 *
 * @author liwenqiang 2018/8/2 21:02
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserGroupService userGroupService;
    private final UserRoleService userRoleService;
    private final AuthorityService authorityService;

    public UserController(UserService userService, UserGroupService userGroupService, UserRoleService userRoleService,
                          AuthorityService authorityService) {
        this.userService = userService;
        this.userGroupService = userGroupService;
        this.userRoleService = userRoleService;
        this.authorityService = authorityService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回204
     */
    @GetMapping
    public ResponseEntity<Page<UserVO>> retrieve(@RequestParam int page, @RequestParam int size, String sort) {
        Page<UserVO> users;
        try {
            users = userService.retrieve(page, size, sort);
        } catch (Exception e) {
            logger.info("Retrieve user occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    /**
     * 查询信息
     *
     * @param username 账户
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{username}")
    public ResponseEntity<UserVO> fetch(@PathVariable String username) {
        UserVO userVO;
        try {
            userVO = userService.fetch(username);
        } catch (Exception e) {
            logger.info("Fetch user occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userVO);
    }

    /**
     * 添加信息
     *
     * @param userDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<UserVO> create(@RequestBody @Valid UserDTO userDTO) {
        UserVO userVO;
        try {
            userVO = userService.create(userDTO);
        } catch (Exception e) {
            logger.error("Save user occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userVO);
    }

    /**
     * 修改信息.
     *
     * @param username 账户
     * @param userDTO  要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{username}")
    public ResponseEntity<UserVO> modify(@PathVariable String username,
                                         @RequestBody @Valid UserDTO userDTO) {
        UserVO userVO;
        try {
            userVO = userService.modify(username, userDTO);
        } catch (Exception e) {
            logger.error("Modify user occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(userVO);
    }

    /**
     * 删除信息
     *
     * @param username 账户
     * @return 如果删除成功，返回200状态码，否则返回417状态码
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> remove(@PathVariable String username) {
        try {
            userService.remove(username);
        } catch (Exception e) {
            logger.error("Remove user occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }


    /**
     * 根据username查询关联分组信息
     *
     * @param username 账号
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{username}/group")
    public ResponseEntity<List<GroupVO>> groups(@PathVariable String username) {
        List<GroupVO> voList;
        try {
            voList = userGroupService.groups(username);
        } catch (Exception e) {
            logger.error("Retrieve user groups occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voList);
    }

    /**
     * 保存用户-分组关联
     *
     * @param username 账户
     * @param groups   分组
     * @return 操作结果
     */
    @PatchMapping("/{username}/group")
    public ResponseEntity<List<UserGroup>> relationGroup(@PathVariable String username, @RequestBody Set<String> groups) {
        List<UserGroup> voList;
        try {
            voList = userGroupService.relation(username, groups);
        } catch (Exception e) {
            logger.error("create user groups occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.accepted().body(voList);
    }

    /**
     * 根据username查询关联角色信息
     *
     * @param username 用户username
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{username}/role")
    public ResponseEntity<List<RoleVO>> roles(@PathVariable String username) {
        List<RoleVO> voList;
        try {
            voList = userRoleService.roles(username);
        } catch (Exception e) {
            logger.error("Retrieve user roles occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voList);
    }

    /**
     * 保存用户-角色关联
     *
     * @param username 账户
     * @param roles    分组
     * @return 操作结果
     */
    @PatchMapping("/{username}/role")
    public ResponseEntity<List<UserRole>> relationRole(@PathVariable String username, @RequestBody Set<String> roles) {
        List<UserRole> voList;
        try {
            voList = userRoleService.relation(username, roles);
        } catch (Exception e) {
            logger.error("create user groups occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.accepted().body(voList);
    }


    /**
     * 查询关联权限
     *
     * @param username 用户账号
     * @param type     类型
     * @return 查询到的数据，否则返回空
     */
    @GetMapping("/{username}/authority")
    public ResponseEntity<List<TreeNode>> authorities(@PathVariable String username, Character type) {
        List<TreeNode> nodes;
        try {
            nodes = authorityService.authorities(username, type);
        } catch (Exception e) {
            logger.info("Retrieve user authorities tree occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(nodes);
    }
}
