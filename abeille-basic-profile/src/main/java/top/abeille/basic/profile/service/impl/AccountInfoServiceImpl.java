/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.profile.service.impl;

import org.springframework.stereotype.Service;
import top.abeille.basic.profile.dao.AccountInfoDao;
import top.abeille.basic.profile.entity.AccountInfo;
import top.abeille.basic.profile.service.AccountInfoService;

import java.util.List;
import java.util.Optional;

/**
 * 账户信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:27
 **/
@Service
public class AccountInfoServiceImpl implements AccountInfoService {

    private final AccountInfoDao accountInfoDao;

    public AccountInfoServiceImpl(AccountInfoDao accountInfoDao) {
        this.accountInfoDao = accountInfoDao;
    }

    @Override
    public AccountInfo getById(Long id) {
        Optional<AccountInfo> optional = accountInfoDao.findById(id);
        return optional.orElse(null);
    }

    @Override
    public AccountInfo save(AccountInfo entity) {
        return accountInfoDao.save(entity);
    }

    @Override
    public void removeById(Long id) {
        accountInfoDao.deleteById(id);
    }

    @Override
    public void removeInBatch(List<AccountInfo> entities) {
        accountInfoDao.deleteInBatch(entities);
    }
}
