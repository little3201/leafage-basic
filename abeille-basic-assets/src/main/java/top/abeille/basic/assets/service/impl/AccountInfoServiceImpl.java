/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import top.abeille.basic.assets.entity.AccountInfo;
import top.abeille.basic.assets.repository.AccountInfoRepository;
import top.abeille.basic.assets.service.AccountInfoService;

import java.util.List;
import java.util.Optional;

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
    public AccountInfo getByExample(AccountInfo accountInfo) {
        accountInfo.setEnabled(true);
        Optional<AccountInfo> optional = accountInfoRepository.findOne(Example.of(accountInfo));
        return optional.orElse(null);
    }

    @Override
    public AccountInfo save(AccountInfo entity) {
        return accountInfoRepository.save(entity);
    }

    @Override
    public void removeById(Long id) {
        accountInfoRepository.deleteById(id);
    }

    @Override
    public void removeInBatch(List<AccountInfo> entities) {
        accountInfoRepository.deleteInBatch(entities);
    }

    @Override
    public AccountInfo getByAccountId(String accountId) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountId(accountId);
        return this.getByExample(accountInfo);
    }

    @Override
    public void removeByAccountId(String accountId) {
        AccountInfo accountInfo = this.getByAccountId(accountId);
        if (accountInfo != null) {
            accountInfoRepository.deleteById(accountInfo.getId());
            log.info("Remove account with accountId: {}, successful", accountId);
        }
    }
}
