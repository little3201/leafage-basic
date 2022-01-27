package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.AccountRole;
import io.leafage.basic.hypervisor.document.Role;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.AccountRoleRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.AccountRoleService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.naming.NotContextException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class AccountRoleServiceImpl implements AccountRoleService {

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RoleRepository roleRepository;

    public AccountRoleServiceImpl(AccountRepository accountRepository, AccountRoleRepository accountRoleRepository,
                                  RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Flux<AccountVO> accounts(String code) {
        Assert.hasText(code, "code is blank");
        return roleRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(role -> accountRoleRepository.findByRoleIdAndEnabledTrue(role.getId()).flatMap(userRole ->
                        accountRepository.findById(userRole.getAccountId()).map(account -> {
                            AccountVO accountVO = new AccountVO();
                            BeanUtils.copyProperties(account, accountVO);
                            return accountVO;
                        })).switchIfEmpty(Mono.error(NoSuchElementException::new))
                );
    }

    @Override
    public Mono<List<String>> roles(String username) {
        Assert.hasText(username, "username must not blank");
        return accountRepository.getByUsernameAndEnabledTrue(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(user -> accountRoleRepository.findByAccountIdAndEnabledTrue(user.getId()).flatMap(userRole ->
                                roleRepository.findById(userRole.getRoleId()).map(Role::getCode))
                        .switchIfEmpty(Mono.error(NoSuchElementException::new))
                ).collectList();
    }

    @Override
    public Mono<Boolean> relation(String username, Set<String> roles) {
        Assert.hasText(username, "username must not blank");
        Assert.notEmpty(roles, "roles must not empty");
        Flux<AccountRole> flux = accountRepository.getByUsernameAndEnabledTrue(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(user -> {
                    AccountRole accountRole = new AccountRole();
                    accountRole.setAccountId(user.getId());
                    return roleRepository.findByCodeInAndEnabledTrue(roles).switchIfEmpty(Mono.error(NotContextException::new))
                            .map(role -> {
                                accountRole.setRoleId(role.getId());
                                return accountRole;
                            }).switchIfEmpty(Mono.error(NoSuchElementException::new))
                            .collectList().flatMapMany(accountRoleRepository::saveAll);
                });
        return flux.hasElements();
    }
}
