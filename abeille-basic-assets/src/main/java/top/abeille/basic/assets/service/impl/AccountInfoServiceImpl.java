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
import top.abeille.common.basic.AbstractBasicService;

import java.util.Objects;

/**
 * 账户信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:27
 **/
@Service
public class AccountInfoServiceImpl extends AbstractBasicService implements AccountInfoService {

    private final AccountInfoRepository accountInfoRepository;

    public AccountInfoServiceImpl(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    @Override
    public Mono<AccountVO> create(AccountDTO accountDTO) {
        AccountInfo info = new AccountInfo();
        BeanUtils.copyProperties(accountDTO, info);
        info.setBusinessId(this.generateId());
        info.setEnabled(Boolean.TRUE);
        return accountInfoRepository.save(info).map(this::convertOuter);
    }

    @Override
    public Mono<AccountVO> modify(String accountId, AccountDTO accountDTO) {
        Objects.requireNonNull(accountId);
        return fetchById(accountId).flatMap(accountVO -> {
            AccountInfo info = new AccountInfo();
            BeanUtils.copyProperties(accountVO, info);
            return accountInfoRepository.save(info).map(this::convertOuter);
        });
    }

    @Override
    public Mono<AccountVO> fetchById(String accountId) {
        return this.fetchByBusinessId(accountId).map(this::convertOuter);
    }

    @Override
    public Mono<Void> removeById(String accountId) {
        return this.fetchByBusinessId(accountId)
                .flatMap(account -> accountInfoRepository.deleteById(account.getId()));
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<AccountInfo> fetchByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        AccountInfo info = new AccountInfo();
        info.setBusinessId(businessId);
        info.setEnabled(Boolean.TRUE);
        return accountInfoRepository.findOne(Example.of(info));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private AccountVO convertOuter(AccountInfo info) {
        AccountVO outer = new AccountVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
