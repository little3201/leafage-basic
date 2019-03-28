package top.abeille.basic.profile.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.abeille.basic.profile.model.AccountInfoModel;
import top.abeille.basic.profile.service.AccountInfoService;
import top.abeille.basic.profile.dao.AccountInfoDao;

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

    @Autowired
    public AccountInfoServiceImpl(AccountInfoDao accountInfoDao) {
        this.accountInfoDao = accountInfoDao;
    }

    @Override
    public AccountInfoModel getById(Long id) {
        Optional<AccountInfoModel> optional = accountInfoDao.findById(id);
        return optional.orElse(null);
    }

    @Override
    public AccountInfoModel save(AccountInfoModel entity) {
        return accountInfoDao.save(entity);
    }

    @Override
    public void removeById(Long id) {
        accountInfoDao.deleteById(id);
    }

    @Override
    public void removeInBatch(List<AccountInfoModel> entities) {
        accountInfoDao.deleteInBatch(entities);
    }
}
