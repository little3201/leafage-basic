/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.entity.AccountGroup;
import io.leafage.basic.hypervisor.entity.AccountRole;
import io.leafage.basic.hypervisor.service.AccountService;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.service.AccountGroupService;
import io.leafage.basic.hypervisor.service.AccountRoleService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
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
 * 账号信息接口.
 *
 * @author liwenqiang 2022/1/26 15:31
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;
    private final AccountGroupService accountGroupService;
    private final AccountRoleService accountRoleService;
    private final AuthorityService authorityService;

    public AccountController(AccountService accountService, AccountGroupService accountGroupService, AccountRoleService accountRoleService,
                             AuthorityService authorityService) {
        this.accountService = accountService;
        this.accountGroupService = accountGroupService;
        this.accountRoleService = accountRoleService;
        this.authorityService = authorityService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回204
     */
    @GetMapping
    public ResponseEntity<Page<AccountVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Page<AccountVO> accounts;
        try {
            accounts = accountService.retrieve(page, size);
        } catch (Exception e) {
            logger.info("Retrieve user occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accounts);
    }

    /**
     * 查询信息
     *
     * @param username 账户
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{username}")
    public ResponseEntity<AccountVO> fetch(@PathVariable String username) {
        AccountVO accountVO;
        try {
            accountVO = accountService.fetch(username);
        } catch (Exception e) {
            logger.info("Fetch account occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accountVO);
    }

    /**
     * 是否存在
     *
     * @param username 账户
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{username}/exist")
    public ResponseEntity<Boolean> exist(@PathVariable String username) {
        boolean exist;
        try {
            exist = accountService.exist(username);
        } catch (Exception e) {
            logger.info("Query account exist occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(exist);
    }

    /**
     * 修改信息.
     *
     * @param username   账户
     * @param accountDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{username}")
    public ResponseEntity<AccountVO> modify(@PathVariable String username,
                                            @RequestBody @Valid AccountDTO accountDTO) {
        AccountVO accountVO;
        try {
            accountVO = accountService.modify(username, accountDTO);
        } catch (Exception e) {
            logger.error("Modify account occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(accountVO);
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
            accountService.remove(username);
        } catch (Exception e) {
            logger.error("Remove account occurred an error: ", e);
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
            voList = accountGroupService.groups(username);
        } catch (Exception e) {
            logger.error("Retrieve account groups occurred an error: ", e);
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
    public ResponseEntity<List<AccountGroup>> groups(@PathVariable String username, @RequestBody Set<String> groups) {
        List<AccountGroup> voList;
        try {
            voList = accountGroupService.relation(username, groups);
        } catch (Exception e) {
            logger.error("create account groups occurred an error: ", e);
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
            voList = accountRoleService.roles(username);
        } catch (Exception e) {
            logger.error("Retrieve account roles occurred an error: ", e);
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
    public ResponseEntity<List<AccountRole>> roles(@PathVariable String username, @RequestBody Set<String> roles) {
        List<AccountRole> voList;
        try {
            voList = accountRoleService.relation(username, roles);
        } catch (Exception e) {
            logger.error("create account role occurred an error: ", e);
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
