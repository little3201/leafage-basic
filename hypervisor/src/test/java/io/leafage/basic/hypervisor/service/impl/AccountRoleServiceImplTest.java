package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Account;
import io.leafage.basic.hypervisor.entity.AccountRole;
import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.AccountRoleRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * user role service test
 *
 * @author liwenqiang 2021/7/5 17:36
 **/
@ExtendWith(MockitoExtension.class)
class AccountRoleServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountRoleRepository accountRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AccountRoleServiceImpl userRoleService;

    @Test
    void accounts() {
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Role.class));

        AccountRole accountRole = new AccountRole();
        accountRole.setAccountId(1L);
        accountRole.setRoleId(1L);
        given(this.accountRoleRepository.findByRoleId(Mockito.anyLong())).willReturn(Collections.singletonList(accountRole));

        given(this.accountRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Account.class)));

        List<AccountVO> accounts = userRoleService.accounts("test");
        Assertions.assertNotNull(accounts);
    }

    @Test
    void roles() {
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Account.class));

        AccountRole accountRole = new AccountRole();
        accountRole.setAccountId(1L);
        accountRole.setRoleId(1L);
        given(this.accountRoleRepository.findByAccountId(Mockito.anyLong())).willReturn(Collections.singletonList(accountRole));

        given(this.roleRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Role.class)));

        List<RoleVO> roles = userRoleService.roles("test");
        Assertions.assertNotNull(roles);
    }

    @Test
    void relation() {
        Account account = new Account();
        account.setId(1L);
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(account);

        Role role = new Role();
        role.setId(2L);
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(role);

        given(this.accountRoleRepository.saveAllAndFlush(Mockito.anyCollection())).willReturn(Mockito.anyList());

        List<AccountRole> relation = userRoleService.relation("test", Collections.singleton("test"));

        verify(this.accountRoleRepository, times(1)).saveAllAndFlush(Mockito.anyList());
        Assertions.assertNotNull(relation);
    }
}