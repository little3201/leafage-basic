/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.AccountDTO;
import top.abeille.basic.hypervisor.service.AccountService;
import top.abeille.basic.hypervisor.vo.AccountVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 账户信息Controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/account")
public class AccountController extends AbstractController {

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
    public Mono<AccountVO> fetchAccount(@PathVariable String code) {
        return accountService.fetchByCode(code);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param accountDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<AccountVO> createAccount(@RequestBody @Valid AccountDTO accountDTO) {
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
    public Mono<AccountVO> modifyAccount(@PathVariable String code, @RequestBody @Valid AccountDTO accountDTO) {
        return accountService.modify(code, accountDTO);
    }

    /**
     * 根据传入的代码删除信息（逻辑删除）
     *
     * @param code 业务id
     * @return 如果删除数据成功，返回删除后的信息，否则返回417状态码
     */
    @DeleteMapping("/{code}")
    public Mono<Void> removeAccount(@PathVariable String code) {
        return accountService.remove(code);
    }

}
