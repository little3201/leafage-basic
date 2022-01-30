package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.entity.Account;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.service.AccountService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * account service impl.
 *
 * @author liwenqiang 2022/1/26 15:20
 **/
@Service
public class AccountServiceImpl implements AccountService {

    private static final String MESSAGE = "username is blank.";

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Page<AccountVO> retrieve(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountRepository.findByEnabledTrue(pageable).map(this::convertOuter);
    }

    @Override
    public AccountVO unlock(String username) {
        Assert.hasText(username, MESSAGE);
        Account account = accountRepository.getByUsernameAndEnabledTrue(username);
        account.setAccountLocked(false);
        accountRepository.saveAndFlush(account);
        return this.convertOuter(account);
    }

    @Override
    public AccountVO modify(String username, AccountDTO accountDTO) {
        Assert.hasText(username, MESSAGE);
        Account account = accountRepository.getByUsernameAndEnabledTrue(username);
        BeanUtils.copyProperties(accountDTO, account);
        accountRepository.saveAndFlush(account);
        return this.convertOuter(account);
    }

    @Override
    public void remove(String username) {
        Assert.hasText(username, MESSAGE);
        Account account = accountRepository.getByUsernameAndEnabledTrue(username);
        account.setEnabled(false);
        accountRepository.saveAndFlush(account);
    }

    @Override
    public AccountVO fetch(String username) {
        Assert.hasText(username, MESSAGE);
        Account account = accountRepository.getByUsernameAndEnabledTrue(username);
        return this.convertOuter(account);
    }

    @Override
    public boolean exist(String username) {
        return accountRepository.existsByUsername(username);
    }

    /**
     * 转换为输出对象
     *
     * @return ExampleMatcher
     */
    private AccountVO convertOuter(Account account) {
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(account, accountVO);
        return accountVO;
    }
}
