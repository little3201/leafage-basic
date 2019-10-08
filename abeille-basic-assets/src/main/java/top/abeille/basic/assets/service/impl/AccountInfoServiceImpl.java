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
    public Mono<AccountOuter> save(AccountEnter entity) {
        AccountInfo info = new AccountInfo();
        BeanUtils.copyProperties(entity, info);
        AccountOuter outer = new AccountOuter();
        return accountInfoRepository.save(info).map(account -> {
            BeanUtils.copyProperties(account, outer);
            return outer;
        });
    }

    @Override
    public Mono<AccountOuter> getByAccountId(Long accountId) {
        AccountInfo info = new AccountInfo();
        info.setAccountId(accountId);
        info.setEnabled(true);
        AccountOuter outer = new AccountOuter();
        return accountInfoRepository.findOne(Example.of(info)).map(account -> {
            BeanUtils.copyProperties(account, outer);
            return outer;
        });
    }

    @Override
    public Mono<Void> removeByAccountId(Long accountId) {
        AccountInfo info = new AccountInfo();
        info.setAccountId(accountId);
        return accountInfoRepository.findOne(Example.of(info))
                .flatMap(account -> accountInfoRepository.deleteById(account.getId()));
    }
}
