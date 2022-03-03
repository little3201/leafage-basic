package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Account;
import io.leafage.basic.hypervisor.entity.AccountGroup;
import io.leafage.basic.hypervisor.entity.Group;
import io.leafage.basic.hypervisor.repository.AccountGroupRepository;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.GroupVO;
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

/**
 * user group service test
 *
 * @author liwenqiang 2021/7/5 17:36
 **/
@ExtendWith(MockitoExtension.class)
class AccountGroupServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountGroupRepository accountGroupRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private AccountGroupServiceImpl userGroupService;

    @Test
    void users() {
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Group.class));

        AccountGroup accountGroup = new AccountGroup();
        accountGroup.setAccountId(1L);
        accountGroup.setGroupId(1L);
        given(this.accountGroupRepository.findByGroupId(Mockito.anyLong())).willReturn(Collections.singletonList(accountGroup));

        given(this.accountRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Account.class)));

        List<AccountVO> accounts = userGroupService.accounts("test");
        Assertions.assertNotNull(accounts);
    }

    @Test
    void groups() {
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Account.class));

        AccountGroup accountGroup = new AccountGroup();
        accountGroup.setAccountId(1L);
        accountGroup.setGroupId(1L);
        given(this.accountGroupRepository.findByAccountId(Mockito.anyLong())).willReturn(Collections.singletonList(accountGroup));

        given(this.groupRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Group.class)));

        List<GroupVO> groups = userGroupService.groups("test");
        Assertions.assertNotNull(groups);
    }

    @Test
    void relation() {
        Account account = new Account();
        account.setId(1L);
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(account);

        Group group = new Group();
        group.setId(2L);
        group.setPrincipal(1L);
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(group);

        given(this.accountGroupRepository.saveAll(Mockito.anyCollection())).willReturn(Mockito.anyList());

        List<AccountGroup> relation = userGroupService.relation("test", Collections.singleton("test"));
        Assertions.assertNotNull(relation);
    }
}