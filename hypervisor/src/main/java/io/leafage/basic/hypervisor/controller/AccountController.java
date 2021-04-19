/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.service.AccountService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.springframework.http.HttpStatus;
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

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<AccountVO> fetch(@PathVariable String code) {
        return accountService.fetch(code);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param accountDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountVO> create(@RequestBody @Valid AccountDTO accountDTO) {
        return accountService.create(accountDTO);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code       代码
     * @param accountDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<AccountVO> modify(@PathVariable String code, @RequestBody @Valid AccountDTO accountDTO) {
        return accountService.modify(code, accountDTO);
    }

    /**
     * 根据传入的代码删除信息（逻辑删除）
     *
     * @param code 业务id
     * @return 如果删除数据成功，返回删除后的信息，否则返回417状态码
     */
    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> remove(@PathVariable String code) {
        return accountService.remove(code);
    }

}
