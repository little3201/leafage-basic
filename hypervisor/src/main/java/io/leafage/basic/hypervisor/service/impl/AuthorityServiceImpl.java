/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.repository.AccountRoleRepository;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;
import java.util.*;

/**
 * 权限信息Service 接口实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class AuthorityServiceImpl extends ReactiveAbstractTreeNodeService<Authority> implements AuthorityService {

    private static final String MESSAGE = "code is blank.";

    private final UserRepository userRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(UserRepository userRepository, AccountRoleRepository accountRoleRepository,
                                RoleAuthorityRepository roleAuthorityRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Flux<AuthorityVO> retrieve(int page, int size) {
        return authorityRepository.findByEnabledTrue(PageRequest.of(page, size)).flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
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
        Assert.hasText(username, "username is blank.");
        Mono<User> userMono = userRepository.getByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));

        return userMono.map(user -> accountRoleRepository.findByAccountIdAndEnabledTrue(user.getId()).flatMap(userRole ->
                roleAuthorityRepository.findByRoleIdAndEnabledTrue(userRole.getRoleId()).flatMap(roleAuthority ->
                        authorityRepository.findById(roleAuthority.getAuthorityId())))).flatMapMany(this::convertTree);
    }

    @Override
    public Mono<Boolean> exist(String name) {
        Assert.hasText(name, "name is blank.");
        return authorityRepository.existsByName(name);
    }

    @Override
    public Mono<AuthorityVO> fetch(String code) {
        Assert.hasText(code, MESSAGE);
        return authorityRepository.getByCodeAndEnabledTrue(code).flatMap(this::fetchOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<Long> count() {
        return authorityRepository.count();
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
                }).flatMap(this::convertOuter);
    }

    @Override
    public Mono<AuthorityVO> modify(String code, AuthorityDTO authorityDTO) {
        Assert.hasText(code, MESSAGE);
        return authorityRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(authority -> this.superior(authorityDTO.getSuperior(), authority))
                .flatMap(authority -> {
                    BeanUtils.copyProperties(authorityDTO, authority);
                    return authorityRepository.save(authority);
                }).flatMap(this::convertOuter);
    }

    /**
     * 设置上级
     *
     * @param superior  上级code
     * @param authority 当前对象
     * @return 设置上级后的对象
     */
    private Mono<Authority> superior(String superior, Authority authority) {
        return Mono.just(authority).flatMap(a -> {
            if (StringUtils.hasText(superior)) {
                return authorityRepository.getByCodeAndEnabledTrue(superior).map(s -> {
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
    private Mono<AuthorityVO> fetchOuter(Authority authority) {
        return Mono.just(authority).map(a -> {
            AuthorityVO authorityVO = new AuthorityVO();
            BeanUtils.copyProperties(authority, authorityVO);
            return authorityVO;
        }).flatMap(vo -> {
            if (authority.getSuperior() != null) {
                return authorityRepository.findById(authority.getSuperior()).map(s -> {
                    vo.setSuperior(s.getCode());
                    return vo;
                }).switchIfEmpty(Mono.just(vo));
            }
            return Mono.just(vo);
        });
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param authority 信息
     * @return 输出转换后的vo对象
     */
    private Mono<AuthorityVO> convertOuter(Authority authority) {
        return Mono.just(authority).map(a -> {
            AuthorityVO authorityVO = new AuthorityVO();
            BeanUtils.copyProperties(authority, authorityVO);
            return authorityVO;
        }).flatMap(vo -> roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(authority.getId())
                .switchIfEmpty(Mono.just(0L))
                .map(count -> {
                    vo.setCount(count);
                    return vo;
                })).flatMap(v -> {
            if (authority.getSuperior() != null) {
                return authorityRepository.findById(authority.getSuperior()).map(s -> {
                    v.setSuperior(s.getName());
                    return v;
                }).switchIfEmpty(Mono.just(v));
            }
            return Mono.just(v);
        });
    }

    /**
     * convert Authority to TreeNode
     *
     * @param authorities flux of authority
     * @return TreeNode of Flux
     */
    private Flux<TreeNode> convertTree(Flux<Authority> authorities) {
        return authorities.filter(a -> Objects.isNull(a.getSuperior())).flatMap(authority -> {
            TreeNode treeNode = this.constructNode(authority.getCode(), authority);

            Set<String> expand = new HashSet<>();
            expand.add("icon");
            expand.add("path");

            return this.children(authority, authorities, expand).collectList().map(treeNodes -> {
                treeNode.setChildren(treeNodes);
                return treeNode;
            });
        });
    }

    /**
     * construct tree node
     *
     * @param superior  superior code
     * @param authority data
     * @return tree node
     */
    private TreeNode constructNode(String superior, Authority authority) {
        TreeNode treeNode = new TreeNode(authority.getCode(), authority.getName());
        treeNode.setSuperior(superior);
        Map<String, Object> expand = new HashMap<>();
        expand.put("icon", authority.getIcon());
        expand.put("path", authority.getPath());
        treeNode.setExpand(expand);
        return treeNode;
    }

}
