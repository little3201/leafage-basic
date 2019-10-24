/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.abeille.basic.assets.dto.AccountDTO;
import top.abeille.basic.assets.repository.AccountInfoRepository;
import top.abeille.basic.assets.service.AccountInfoService;

import java.util.List;

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
    public void removeById(Long id) {
        accountInfoRepository.deleteById(id);
    }

    @Override
    public void removeInBatch(List<AccountDTO> entities) {
    }
}
