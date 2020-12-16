/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import top.abeille.basic.assets.dto.AccountDTO;
import top.abeille.basic.assets.entity.Account;
import top.abeille.basic.assets.repository.AccountRepository;
import top.abeille.basic.assets.service.AccountService;

import java.util.List;
import java.util.Optional;

/**
 * 账户信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:27
 **/
@Service
public class AccountServiceImpl implements AccountService {

    /**
     * 开启日志
     */
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void remove(String code) {
        Optional<Account> optional = this.fetchInfo(code);
        if (optional.isPresent()) {
            Account info = optional.get();
            log.info("删除code为：{}的账户", code);
            accountRepository.deleteById(info.getId());
        }
    }

    @Override
    public void removeAll(List<AccountDTO> entities) {
    }

    /**
     * 根据代码查信息
     *
     * @param code 代码
     * @return 数据库对象信息
     */
    private Optional<Account> fetchInfo(String code) {
        Account account = new Account();
        account.setCode(code);
        return accountRepository.findOne(Example.of(account));
    }
}
