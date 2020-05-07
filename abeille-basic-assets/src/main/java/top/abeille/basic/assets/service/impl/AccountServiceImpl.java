/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.constant.PrefixEnum;
import top.abeille.basic.assets.document.AccountInfo;
import top.abeille.basic.assets.dto.AccountDTO;
import top.abeille.basic.assets.repository.AccountRepository;
import top.abeille.basic.assets.service.AccountService;
import top.abeille.basic.assets.vo.AccountVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 账户信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:27
 **/
@Service
public class AccountServiceImpl extends AbstractBasicService implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<AccountVO> create(AccountDTO accountDTO) {
        AccountInfo info = new AccountInfo();
        BeanUtils.copyProperties(accountDTO, info);
        info.setBusinessId(PrefixEnum.AC + this.generateId());
        info.setEnabled(Boolean.TRUE);
        info.setModifyTime(LocalDateTime.now());
        return accountRepository.insert(info).map(this::convertOuter);
    }

    @Override
    public Mono<AccountVO> modify(String accountId, AccountDTO accountDTO) {
        Objects.requireNonNull(accountId);
        return this.fetchByBusinessId(accountId).flatMap(accountVO -> {
            AccountInfo info = new AccountInfo();
            BeanUtils.copyProperties(accountVO, info);
            return accountRepository.save(info).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        return this.fetchInfo(businessId)
                .flatMap(account -> accountRepository.deleteById(account.getId()));
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    @Override
    public Mono<AccountVO> fetchByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).map(this::convertOuter);
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<AccountInfo> fetchInfo(String businessId) {
        AccountInfo info = new AccountInfo();
        info.setBusinessId(businessId);
        info.setEnabled(Boolean.TRUE);
        return accountRepository.findOne(Example.of(info));
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
