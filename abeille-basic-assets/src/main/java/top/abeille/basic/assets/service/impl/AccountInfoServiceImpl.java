/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.AccountInfo;
import top.abeille.basic.assets.dto.AccountDTO;
import top.abeille.basic.assets.repository.AccountInfoRepository;
import top.abeille.basic.assets.service.AccountInfoService;
import top.abeille.basic.assets.vo.AccountVO;

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
    public Mono<AccountVO> create(AccountDTO accountDTO) {
        AccountInfo info = new AccountInfo();
        BeanUtils.copyProperties(accountDTO, info);
        return accountInfoRepository.save(info).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<AccountVO> modify(String accountId, AccountDTO accountDTO) {
        AccountInfo info = new AccountInfo();
        BeanUtils.copyProperties(accountDTO, info);
        info.setAccountId(accountId);
        return accountInfoRepository.save(info).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<AccountVO> fetchById(String accountId) {
        AccountInfo info = new AccountInfo();
        info.setAccountId(accountId);
        info.setEnabled(true);
        return accountInfoRepository.findOne(Example.of(info)).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<Void> removeById(String accountId) {
        AccountInfo info = new AccountInfo();
        info.setAccountId(accountId);
        return accountInfoRepository.findOne(Example.of(info))
                .flatMap(account -> accountInfoRepository.deleteById(account.getId()));
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param info 信息
     * @return AccountVO 输出对象
     */
    private AccountVO convertOuter(AccountInfo info) {
        AccountVO outer = new AccountVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
