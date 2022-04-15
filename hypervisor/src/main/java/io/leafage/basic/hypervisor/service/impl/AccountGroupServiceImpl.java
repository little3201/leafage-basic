package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Account;
import io.leafage.basic.hypervisor.entity.AccountGroup;
import io.leafage.basic.hypervisor.entity.Group;
import io.leafage.basic.hypervisor.repository.AccountGroupRepository;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.service.AccountGroupService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * account group service impl.
 *
 * @author liwenqiang 2021/11/27 14:18
 **/
@Service
public class AccountGroupServiceImpl implements AccountGroupService {

    private final AccountRepository accountRepository;
    private final AccountGroupRepository accountGroupRepository;
    private final GroupRepository groupRepository;

    public AccountGroupServiceImpl(AccountRepository accountRepository, AccountGroupRepository accountGroupRepository,
                                   GroupRepository groupRepository) {
        this.accountRepository = accountRepository;
        this.accountGroupRepository = accountGroupRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public List<AccountVO> accounts(String code) {
        Group group = groupRepository.getByCodeAndEnabledTrue(code);
        if (group == null) {
            return Collections.emptyList();
        }
        List<AccountGroup> accountGroups = accountGroupRepository.findByGroupId(group.getId());
        return accountGroups.stream().map(userGroup -> accountRepository.findById(userGroup.getAccountId()))
                .map(Optional::orElseThrow).map(account -> {
                    AccountVO accountVO = new AccountVO();
                    BeanUtils.copyProperties(account, accountVO);
                    return accountVO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<GroupVO> groups(String username) {
        Assert.hasText(username, "username is blank");
        Account account = accountRepository.getByUsernameAndEnabledTrue(username);
        if (account == null) {
            return Collections.emptyList();
        }
        List<AccountGroup> accountGroups = accountGroupRepository.findByAccountId(account.getId());
        if (CollectionUtils.isEmpty(accountGroups)) {
            return Collections.emptyList();
        }
        return accountGroups.stream().map(userGroup -> groupRepository.findById(userGroup.getGroupId()))
                .map(Optional::orElseThrow).map(role -> {
                    GroupVO groupVO = new GroupVO();
                    BeanUtils.copyProperties(role, groupVO);
                    return groupVO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<AccountGroup> relation(String username, Set<String> groups) {
        Assert.hasText(username, "username is blank");
        Assert.notNull(groups, "groups is null");
        Account account = accountRepository.getByUsernameAndEnabledTrue(username);
        if (account == null) {
            return Collections.emptyList();
        }
        List<AccountGroup> accountGroups = groups.stream().map(s -> {
            Group group = groupRepository.getByCodeAndEnabledTrue(s);
            AccountGroup userRole = new AccountGroup();
            userRole.setAccountId(account.getId());
            userRole.setGroupId(group.getId());
            return userRole;
        }).collect(Collectors.toList());
        return accountGroupRepository.saveAllAndFlush(accountGroups);
    }
}
