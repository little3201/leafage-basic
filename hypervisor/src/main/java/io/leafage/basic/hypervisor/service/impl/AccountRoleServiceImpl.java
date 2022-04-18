package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Account;
import io.leafage.basic.hypervisor.entity.AccountRole;
import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.AccountRoleRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.AccountRoleService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import top.leafage.common.basic.ValidMessage;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * account role service impl.
 *
 * @author liwenqiang 2021/11/27 14:18
 **/
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
    public List<AccountVO> accounts(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Role role = roleRepository.getByCodeAndEnabledTrue(code);
        if (role == null) {
            return Collections.emptyList();
        }
        List<AccountRole> accountRoles = accountRoleRepository.findByRoleId(role.getId());
        return accountRoles.stream().map(userRole -> accountRepository.findById(userRole.getAccountId()))
                .map(Optional::orElseThrow).map(account -> {
                    AccountVO accountVO = new AccountVO();
                    BeanUtils.copyProperties(account, accountVO);
                    return accountVO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<RoleVO> roles(String username) {
        Assert.hasText(username, ValidMessage.USERNAME_NOT_BLANK);
        Account account = accountRepository.getByUsernameAndEnabledTrue(username);
        if (account == null) {
            return Collections.emptyList();
        }
        List<AccountRole> accountRoles = accountRoleRepository.findByAccountId(account.getId());
        if (!CollectionUtils.isEmpty(accountRoles)) {
            return accountRoles.stream().map(userRole -> roleRepository.findById(userRole.getRoleId()))
                    .map(Optional::orElseThrow).map(role -> {
                        RoleVO roleVO = new RoleVO();
                        BeanUtils.copyProperties(role, roleVO);
                        return roleVO;
                    }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<AccountRole> relation(String username, Set<String> roles) {
        Assert.hasText(username, ValidMessage.USERNAME_NOT_BLANK);
        Assert.notNull(roles, "roles is empty.");
        Account account = accountRepository.getByUsernameAndEnabledTrue(username);
        if (account == null) {
            return Collections.emptyList();
        }
        List<AccountRole> accountRoles = roles.stream().map(s -> {
            Role role = roleRepository.getByCodeAndEnabledTrue(s);
            AccountRole accountRole = new AccountRole();
            accountRole.setAccountId(account.getId());
            accountRole.setRoleId(role.getId());
            return accountRole;
        }).collect(Collectors.toList());
        return accountRoleRepository.saveAllAndFlush(accountRoles);
    }
}
