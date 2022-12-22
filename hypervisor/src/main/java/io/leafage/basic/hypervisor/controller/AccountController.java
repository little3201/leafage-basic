/*
 *  Copyright 2018-2022 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.service.AccountGroupService;
import io.leafage.basic.hypervisor.service.AccountRoleService;
import io.leafage.basic.hypervisor.service.AccountService;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;

import java.util.List;
import java.util.Set;

/**
 * account controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;
    private final AccountGroupService accountGroupService;
    private final AccountRoleService accountRoleService;
    private final AuthorityService authorityService;

    public AccountController(AccountService accountService, AccountGroupService accountGroupService,
                             AccountRoleService accountRoleService, AuthorityService authorityService) {
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
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Mono<Page<AccountVO>>> retrieve(@RequestParam int page, @RequestParam int size) {
        Mono<Page<AccountVO>> pageMono;
        try {
            pageMono = accountService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve account occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pageMono);
    }

    /**
     * 根据 username 查询
     *
     * @param username 账号
     * @return 查询到的信息，异常时返回204状态码
     */
    @GetMapping("/{username}")
    public ResponseEntity<Mono<AccountVO>> fetch(@PathVariable String username) {
        Mono<AccountVO> voMono;
        try {
            voMono = accountService.fetch(username);
        } catch (Exception e) {
            logger.error("Fetch account occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 添加信息
     *
     * @param accountDTO 要添加的数据
     * @return 添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<AccountVO>> create(@RequestBody @Valid AccountDTO accountDTO) {
        Mono<AccountVO> voMono;
        try {
            voMono = accountService.create(accountDTO);
        } catch (Exception e) {
            logger.error("Create account occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 修改信息
     *
     * @param username   账号
     * @param accountDTO 要修改的数据
     * @return 修改后的信息，否则返回304状态码
     */
    @PutMapping("/{username}")
    public ResponseEntity<Mono<AccountVO>> modify(@PathVariable String username, @RequestBody @Valid AccountDTO accountDTO) {
        Mono<AccountVO> voMono;
        try {
            voMono = accountService.modify(username, accountDTO);
        } catch (Exception e) {
            logger.error("Modify account occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

    /**
     * 解锁
     *
     * @param username 账号
     * @return 结果，否则返回304状态码
     */
    @PatchMapping("/{username}")
    public ResponseEntity<Mono<Boolean>> unlock(@PathVariable String username) {
        Mono<Boolean> mono;
        try {
            mono = accountService.unlock(username);
        } catch (Exception e) {
            logger.error("Modify account occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(mono);
    }

    /**
     * 删除信息（逻辑删除）
     *
     * @param username 账号
     * @return 200状态码，异常时返回417状态码
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Mono<Void>> remove(@PathVariable String username) {
        Mono<Void> voidMono;
        try {
            voidMono = accountService.remove(username);
        } catch (Exception e) {
            logger.error("Remove account occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok(voidMono);
    }

    /**
     * 查询关联分组
     *
     * @param username 账号
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{username}/groups")
    public ResponseEntity<Mono<List<String>>> group(@PathVariable String username) {
        Mono<List<String>> listMono;
        try {
            listMono = accountGroupService.groups(username);
        } catch (Exception e) {
            logger.error("Retrieve user groups occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listMono);
    }

    /**
     * 关联分组
     *
     * @param username 账户
     * @param groups   分组
     * @return 操作结果
     */
    @PatchMapping("/{username}/groups")
    public ResponseEntity<Mono<Boolean>> group(@PathVariable String username, @RequestBody Set<String> groups) {
        Mono<Boolean> voMono;
        try {
            voMono = accountGroupService.relation(username, groups);
        } catch (Exception e) {
            logger.error("create user groups occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

    /**
     * 查询关联角色
     *
     * @param username 用户username
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{username}/roles")
    public ResponseEntity<Mono<List<String>>> role(@PathVariable String username) {
        Mono<List<String>> listMono;
        try {
            listMono = accountRoleService.roles(username);
        } catch (Exception e) {
            logger.error("Retrieve user roles occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listMono);
    }

    /**
     * 关联角色
     *
     * @param username 账户
     * @param roles    分组
     * @return 操作结果
     */
    @PatchMapping("/{username}/roles")
    public ResponseEntity<Mono<Boolean>> role(@PathVariable String username, @RequestBody Set<String> roles) {
        Mono<Boolean> voMono;
        try {
            voMono = accountRoleService.relation(username, roles);
        } catch (Exception e) {
            logger.error("create user groups occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

    /**
     * 查询关联权限
     *
     * @param username 用户username
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{username}/authorities")
    public ResponseEntity<Flux<TreeNode>> authority(@PathVariable String username) {
        Flux<TreeNode> authorities;
        try {
            authorities = authorityService.authorities(username);
        } catch (Exception e) {
            logger.error("Retrieve user authorities tree occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authorities);
    }
}
