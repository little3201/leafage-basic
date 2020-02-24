/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.AccountDTO;
import top.abeille.basic.assets.service.AccountInfoService;
import top.abeille.basic.assets.vo.AccountVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 账户信息Controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/account")
public class AccountInfoController extends AbstractController {

    private final AccountInfoService accountInfoService;

    public AccountInfoController(AccountInfoService accountInfoService) {
        this.accountInfoService = accountInfoService;
    }

    /**
     * 根据传入的业务id: businessId 查询信息
     *
     * @param businessId 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{businessId}")
    public Mono<ResponseEntity<AccountVO>> fetchAccount(@PathVariable String businessId) {
        return accountInfoService.fetchById(businessId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param accountDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<ResponseEntity<AccountVO>> createAccount(@RequestBody @Valid AccountDTO accountDTO) {
        return accountInfoService.create(accountDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId 业务id
     * @param accountDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{accountId}")
    public Mono<ResponseEntity<AccountVO>> modifyAccount(@PathVariable String businessId, @RequestBody @Valid AccountDTO accountDTO) {
        return accountInfoService.modify(businessId, accountDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_MODIFIED));
    }

    /**
     * 根据传入的业务id: businessId 删除信息（逻辑删除）
     *
     * @param businessId 业务id
     * @return 如果删除数据成功，返回删除后的信息，否则返回417状态码
     */
    @DeleteMapping("/{accountId}")
    public Mono<ResponseEntity<Void>> removeAccount(@PathVariable String businessId) {
        return accountInfoService.removeById(businessId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }
}
