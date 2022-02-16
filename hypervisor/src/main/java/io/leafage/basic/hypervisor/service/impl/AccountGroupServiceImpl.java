package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Group;
import io.leafage.basic.hypervisor.document.AccountGroup;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.repository.AccountGroupRepository;
import io.leafage.basic.hypervisor.service.AccountGroupService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * account group service impl
 *
 * @author liwenqiang 2018/12/17 19:26
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
    public Flux<AccountVO> accounts(String code) {
        return groupRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(group -> accountGroupRepository.findByGroupIdAndEnabledTrue(group.getId()).flatMap(userGroup ->
                        accountRepository.findById(userGroup.getAccountId()).map(account -> {
                            AccountVO accountVO = new AccountVO();
                            BeanUtils.copyProperties(account, accountVO);
                            return accountVO;
                        })).switchIfEmpty(Mono.error(NoSuchElementException::new))
                );
    }

    @Override
    public Mono<List<String>> groups(String username) {
        return accountRepository.getByUsernameAndEnabledTrue(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(account -> accountGroupRepository.findByAccountIdAndEnabledTrue(account.getId())
                        .flatMap(accountGroup -> groupRepository.findById(accountGroup.getGroupId()).map(Group::getCode))
                        .switchIfEmpty(Mono.error(NoSuchElementException::new))
                ).collectList();
    }

    @Override
    public Mono<Boolean> relation(String username, Set<String> groups) {
        Assert.hasText(username, "username is blank");
        Assert.notNull(groups, "groups is null");
        Flux<AccountGroup> flux = accountRepository.getByUsernameAndEnabledTrue(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(user -> {
                    AccountGroup accountGroup = new AccountGroup();
                    accountGroup.setAccountId(user.getId());
                    return groupRepository.findByCodeInAndEnabledTrue(groups).map(group -> {
                                accountGroup.setGroupId(group.getId());
                                return accountGroup;
                            }).switchIfEmpty(Mono.error(NoSuchElementException::new))
                            .collectList().flatMapMany(accountGroupRepository::saveAll);
                });
        return flux.hasElements();
    }
}
