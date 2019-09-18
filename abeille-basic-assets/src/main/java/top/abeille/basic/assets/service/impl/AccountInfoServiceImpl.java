/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.entity.AccountInfo;
import top.abeille.basic.assets.repository.AccountInfoRepository;
import top.abeille.basic.assets.service.AccountInfoService;

/**
 * 账户信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:27
 **/
@Service
public class AccountInfoServiceImpl implements AccountInfoService {

    /**
     * 开启日志
     */
    private static final Logger log = LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    private final AccountInfoRepository accountInfoRepository;

    public AccountInfoServiceImpl(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    @Override
    public Mono<AccountInfo> getByExample(AccountInfo accountInfo) {
        accountInfo.setEnabled(true);
        return accountInfoRepository.findOne(Example.of(accountInfo));
    }

    @Override
    public Mono<AccountInfo> save(AccountInfo entity) {
        return accountInfoRepository.save(entity);
    }

    @Override
    public Mono<Void> removeById(Long id) {
        return accountInfoRepository.deleteById(id);
    }

    @Override
    public Mono<AccountInfo> getByAccountId(String accountId) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountId(accountId);
        return this.getByExample(accountInfo);
    }

    @Override
    public Mono<Void> removeByAccountId(String accountId) {
        AccountInfo accountInfo = this.getByAccountId(accountId).block();
        if (accountInfo != null) {
            return accountInfoRepository.deleteById(accountInfo.getId());
        }
        return Mono.empty();
    }
}
