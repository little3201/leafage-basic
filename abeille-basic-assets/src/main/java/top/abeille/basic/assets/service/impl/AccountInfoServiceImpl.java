/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.entity.AccountInfo;
import top.abeille.basic.assets.repository.AccountInfoRepository;
import top.abeille.basic.assets.service.AccountInfoService;
import top.abeille.basic.assets.vo.enter.AccountEnter;
import top.abeille.basic.assets.vo.outer.AccountOuter;

import java.util.Objects;

/**
 * 账户信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:27
 **/
@Service
public class AccountInfoServiceImpl implements AccountInfoService {

    private final AccountInfoRepository accountInfoRepository;

    public AccountInfoServiceImpl(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    @Override
    public Mono<AccountOuter> save(Long accountId, AccountEnter entity) {
        AccountInfo info = new AccountInfo();
        BeanUtils.copyProperties(entity, info);
        return accountInfoRepository.save(info).map(this::convertOuter);
    }

    @Override
    public Mono<AccountOuter> getByAccountId(Long accountId) {
        AccountInfo info = new AccountInfo();
        info.setAccountId(accountId);
        info.setEnabled(true);
        return accountInfoRepository.findOne(Example.of(info)).map(this::convertOuter);
    }

    @Override
    public Mono<Void> removeByAccountId(Long accountId) {
        AccountInfo info = new AccountInfo();
        info.setAccountId(accountId);
        return accountInfoRepository.findOne(Example.of(info))
                .flatMap(account -> accountInfoRepository.deleteById(account.getId()));
    }

    private AccountOuter convertOuter(AccountInfo info) {
        if (Objects.isNull(info)) {
            return null;
        }
        AccountOuter outer = new AccountOuter();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
