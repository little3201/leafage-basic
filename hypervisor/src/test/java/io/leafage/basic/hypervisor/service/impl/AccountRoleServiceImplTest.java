package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.document.Role;
import io.leafage.basic.hypervisor.document.AccountRole;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.AccountRoleRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.BDDMockito.given;

/**
 * user-role接口测试
 *
 * @author liwenqiang 2021/6/14 11:10
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
        Role role = new Role();
        role.setId(new ObjectId());
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(role));

        AccountRole accountRole = new AccountRole();
        accountRole.setRoleId(role.getId());
        accountRole.setAccountId(new ObjectId());
        given(this.accountRoleRepository.findByRoleIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Flux.just(accountRole));

        given(this.accountRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Account.class)));
        StepVerifier.create(userRoleService.accounts("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void roles() {
        Account account = new Account();
        account.setId(new ObjectId());
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(account));

        AccountRole accountRole = new AccountRole();
        accountRole.setAccountId(account.getId());
        accountRole.setRoleId(new ObjectId());
        given(this.accountRoleRepository.findByAccountIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Flux.just(accountRole));

        Role role = new Role();
        role.setCode("21612OL34");
        given(this.roleRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(role));

        StepVerifier.create(userRoleService.roles("little3201")).expectNextCount(1).verifyComplete();
    }

    @Test
    void relation() {
        Account account = new Account();
        account.setId(new ObjectId());
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(account));

        Role role = new Role();
        role.setId(new ObjectId());
        given(this.roleRepository.findByCodeInAndEnabledTrue(Mockito.anyCollection())).willReturn(Flux.just(role));

        given(this.accountRoleRepository.saveAll(Mockito.anyCollection())).willReturn(Flux.just(Mockito.mock(AccountRole.class)));

        StepVerifier.create(userRoleService.relation("little3201", Collections.singleton("21612OL34")))
                .expectNextCount(1).verifyComplete();
    }
}