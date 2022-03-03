package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.entity.Account;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * account service test
 *
 * @author liwenqiang 2022/1/26 15:37
 **/
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void retrieve() {
        Account account = new Account();
        account.setUsername("little3201");
        account.setAvatar("./avatar.jpg");
        account.setAccountExpiresAt(LocalDateTime.now().plusMonths(1));
        account.setAccountLocked(false);
        account.setCredentialsExpiresAt(LocalDateTime.now().plusHours(2));
        Page<Account> accountsPage = new PageImpl<>(List.of(account));
        given(this.accountRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(accountsPage);

        Page<AccountVO> voPage = accountService.retrieve(0, 2);

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Account.class));

        AccountVO accountVO = accountService.fetch("test");

        Assertions.assertNotNull(accountVO);
    }

    @Test
    void modify() {
        // 根据code查询信息
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Account.class));
        Account account = new Account();
        account.setId(1L);
        account.setUsername("test");
        account.setNickname("测试");
        // 保存更新信息
        given(this.accountRepository.saveAndFlush(Mockito.any(Account.class))).willReturn(account);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNickname("管理员");
        accountDTO.setAvatar("./avatar.jpg");
        account.setAccountExpiresAt(LocalDateTime.now().plusMonths(1));
        account.setAccountLocked(false);
        account.setCredentialsExpiresAt(LocalDateTime.now().plusHours(2));
        accountService.modify("test", accountDTO);

        verify(accountRepository, Mockito.times(1)).saveAndFlush(Mockito.any(Account.class));
    }

    @Test
    void remove() {
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Account.class));

        accountService.remove("test");

        verify(this.accountRepository, times(1)).saveAndFlush(Mockito.any(Account.class));
    }

    @Test
    void exist() {
        given(this.accountRepository.existsByUsername(Mockito.anyString())).willReturn(true);

        boolean exist = accountService.exist("test");

        Assertions.assertTrue(exist);
    }

    @Test
    void exist_false() {
        given(this.accountRepository.existsByUsername(Mockito.anyString())).willReturn(false);

        boolean exist = accountService.exist("test");

        Assertions.assertFalse(exist);
    }

    @Test
    void unlock() {
        Account account = new Account();
        account.setId(1L);
        account.setUsername("test");
        account.setNickname("测试");
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(account);

        given(this.accountRepository.saveAndFlush(Mockito.any(Account.class))).willReturn(Mockito.mock(Account.class));

        AccountVO accountVO = accountService.unlock("test");

        Assertions.assertNotNull(accountVO);
    }

}