/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import top.abeille.basic.assets.dto.AccountDTO;
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
    public void removeById(String businessId) {
        Optional<AccountInfo> optional = this.fetchInfo(businessId);
        if (optional.isPresent()) {
            AccountInfo info = optional.get();
            log.info("删除业务ID为：{}的账户", businessId);
            accountInfoRepository.deleteById(info.getId());
        }
    }

    @Override
    public void removeInBatch(List<AccountDTO> entities) {
    }

    /**
     * 根据业务ID查信息
     *
     * @param businessId 业务ID
     * @return 数据库对象信息
     */
    private Optional<AccountInfo> fetchInfo(String businessId) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setBusinessId(businessId);
        return accountInfoRepository.findOne(Example.of(accountInfo));
    }
}
