/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.bo.BasicBO;
import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.AccountRoleRepository;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import top.leafage.common.basic.ValidMessage;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * authority service impl
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class AuthorityServiceImpl extends ReactiveAbstractTreeNodeService<Authority> implements AuthorityService {

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AccountRepository accountRepository, AccountRoleRepository accountRoleRepository,
                                RoleAuthorityRepository roleAuthorityRepository, AuthorityRepository authorityRepository) {
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Mono<Page<AuthorityVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<AuthorityVO> voFlux = authorityRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuterWithCount);

        Mono<Long> count = authorityRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Flux<TreeNode> tree() {
        Flux<Authority> authorities = authorityRepository.findByEnabledTrue()
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
        return this.convertTree(authorities);
    }

    @Override
    public Flux<AuthorityVO> retrieve() {
        return authorityRepository.findByTypeAndEnabledTrue('M').flatMap(this::convertOuter);
    }

    @Override
    public Flux<TreeNode> authorities(String username) {
        Assert.hasText(username, ValidMessage.USERNAME_NOT_BLANK);
        Mono<Account> accountMono = accountRepository.getByUsernameAndEnabledTrue(username)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));

        return accountMono.map(account -> accountRoleRepository.findByAccountIdAndEnabledTrue(account.getId())
                        .flatMap(userRole -> roleAuthorityRepository.findByRoleIdAndEnabledTrue(userRole.getRoleId())
                                .flatMap(roleAuthority -> authorityRepository.findById(roleAuthority.getAuthorityId()))))
                .flatMapMany(this::convertTree);
    }

    @Override
    public Mono<Boolean> exist(String name) {
        Assert.hasText(name, ValidMessage.NAME_NOT_BLANK);
        return authorityRepository.existsByName(name);
    }

    @Override
    public Mono<AuthorityVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return authorityRepository.getByCodeAndEnabledTrue(code).flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<AuthorityVO> create(AuthorityDTO authorityDTO) {
        return Mono.just(authorityDTO).map(dto -> {
                    Authority authority = new Authority();
                    BeanUtils.copyProperties(authorityDTO, authority);
                    authority.setCode(this.generateCode());
                    return authority;
                }).flatMap(authority -> this.superior(authorityDTO.getSuperior(), authority))
                .switchIfEmpty(Mono.error(NoSuchElementException::new)).flatMap(authority -> {
                    BeanUtils.copyProperties(authorityDTO, authority);
                    return authorityRepository.insert(authority);
                }).flatMap(this::convertOuterWithCount);
    }

    @Override
    public Mono<AuthorityVO> modify(String code, AuthorityDTO authorityDTO) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return authorityRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(authority -> this.superior(authorityDTO.getSuperior(), authority))
                .flatMap(authority -> {
                    BeanUtils.copyProperties(authorityDTO, authority);
                    return authorityRepository.save(authority);
                }).flatMap(this::convertOuterWithCount);
    }

    /**
     * 设置上级
     *
     * @param superior  上级code
     * @param authority 当前对象
     * @return 设置上级后的对象
     */
    private Mono<Authority> superior(BasicBO<String> superior, Authority authority) {
        return Mono.just(authority).flatMap(a -> {
            if (Objects.nonNull(superior)) {
                return authorityRepository.getByCodeAndEnabledTrue(superior.getCode()).map(s -> {
                            authority.setSuperior(s.getId());
                            return authority;
                        })
                        .switchIfEmpty(Mono.error(NoSuchElementException::new));
            }
            return Mono.just(a);
        });
    }

    /**
     * 对象转换为输出结果对象(单条查询)
     *
     * @param authority 信息
     * @return 输出转换后的vo对象
     */
    private Mono<AuthorityVO> convertOuter(Authority authority) {
        return Mono.just(authority).map(a -> {
            AuthorityVO authorityVO = new AuthorityVO();
            BeanUtils.copyProperties(authority, authorityVO);
            return authorityVO;
        }).flatMap(vo -> superior(authority.getSuperior(), vo));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param authority 信息
     * @return 输出转换后的vo对象
     */
    private Mono<AuthorityVO> convertOuterWithCount(Authority authority) {
        return this.convertOuter(authority).flatMap(vo ->
                roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(authority.getId())
                        .switchIfEmpty(Mono.just(0L))
                        .map(count -> {
                            vo.setCount(count);
                            return vo;
                        }));
    }

    /**
     * 转换superior
     *
     * @param superiorId superior主键
     * @param vo         vo
     * @return 设置后的vo
     */
    private Mono<AuthorityVO> superior(ObjectId superiorId, AuthorityVO vo) {
        if (superiorId != null) {
            return authorityRepository.findById(superiorId).map(superior -> {
                BasicBO<String> basicVO = new BasicBO<>();
                BeanUtils.copyProperties(superior, basicVO);
                vo.setSuperior(basicVO);
                return vo;
            }).switchIfEmpty(Mono.just(vo));
        }
        return Mono.just(vo);
    }

    /**
     * convert Authority to TreeNode
     *
     * @param authorities flux of authority
     * @return TreeNode of Flux
     */
    private Flux<TreeNode> convertTree(Flux<Authority> authorities) {
        Set<String> expand = new HashSet<>();
        expand.add("icon");
        expand.add("path");
        return this.convert(authorities, expand);
    }

}
