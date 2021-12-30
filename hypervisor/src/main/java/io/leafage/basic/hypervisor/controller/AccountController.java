/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.service.AccountService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * 账户信息Controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/account")
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
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
     * @param code       代码
     * @param accountDTO 要修改的数据
     * @return 修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<Mono<AccountVO>> modify(@PathVariable String code, @RequestBody @Valid AccountDTO accountDTO) {
        Mono<AccountVO> voMono;
        try {
            voMono = accountService.modify(code, accountDTO);
        } catch (Exception e) {
            logger.error("Modify account occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

    /**
     * 删除信息（逻辑删除）
     *
     * @param code 代码
     * @return 200状态码，异常时返回417状态码
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Mono<Void>> remove(@PathVariable String code) {
        Mono<Void> voidMono;
        try {
            voidMono = accountService.remove(code);
        } catch (Exception e) {
            logger.error("Remove account occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok(voidMono);
    }

}
