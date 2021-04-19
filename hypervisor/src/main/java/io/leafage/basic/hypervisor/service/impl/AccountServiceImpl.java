/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.service.AccountService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.common.basic.AbstractBasicService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

/**
 * 账户信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:27
 **/
@Service
public class AccountServiceImpl extends AbstractBasicService implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<AccountVO> create(AccountDTO accountDTO) {
        Account info = new Account();
        BeanUtils.copyProperties(accountDTO, info);
        info.setCode(this.generateCode());
        return accountRepository.insert(info).map(this::convertOuter);
    }

    @Override
    public Mono<AccountVO> modify(String code, AccountDTO accountDTO) {
        Assert.hasText(code, "code is blank");
        return accountRepository.getByCodeAndEnabledTrue(code).flatMap(accountVO -> {
            Account info = new Account();
            BeanUtils.copyProperties(accountDTO, info);
            return accountRepository.save(info).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> remove(String code) {
        Assert.hasText(code, "code is blank");
        return accountRepository.getByCodeAndEnabledTrue(code)
                .flatMap(account -> accountRepository.deleteById(account.getId()));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private AccountVO convertOuter(Account info) {
        AccountVO outer = new AccountVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
