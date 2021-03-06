/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;
import top.leafage.common.basic.TreeNode;

import java.util.*;

/**
 * 权限信息Service 接口实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class AuthorityServiceImpl extends AbstractBasicService implements AuthorityService {

    private final RoleAuthorityRepository roleAuthorityRepository;
    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(RoleAuthorityRepository roleAuthorityRepository, AuthorityRepository authorityRepository) {
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Flux<AuthorityVO> retrieve(Character type) {
        return authorityRepository.findByTypeAndEnabledTrue(type).flatMap(this::convertOuter);
    }

    @Override
    public Flux<AuthorityVO> retrieve(int page, int size) {
        return authorityRepository.findByEnabledTrue(PageRequest.of(page, size)).flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Flux<TreeNode> tree() {
        Flux<Authority> authorities = authorityRepository.findByTypeAndEnabledTrue('M')
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
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

    @Override
    public Mono<AuthorityVO> fetch(String code) {
        Assert.hasText(code, "code is blank");
        return authorityRepository.getByCodeAndEnabledTrue(code).flatMap(this::fetchOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<Long> count() {
        return authorityRepository.count();
    }

    @Override
    public Mono<AuthorityVO> create(AuthorityDTO authorityDTO) {
        Mono<Authority> authorityMono = Mono.just(authorityDTO).map(dto -> {
            Authority authority = new Authority();
            BeanUtils.copyProperties(authorityDTO, authority);
            authority.setCode(this.generateCode());
            return authority;
        });
        // 设置上级
        authorityMono = this.superior(authorityDTO.getSuperior(), authorityMono);
        return authorityMono.flatMap(authorityRepository::insert).flatMap(this::convertOuter);
    }

    @Override
    public Mono<AuthorityVO> modify(String code, AuthorityDTO authorityDTO) {
        Assert.hasText(code, "code is blank");
        Mono<Authority> authorityMono = authorityRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
        // 设置上级
        authorityMono = this.superior(authorityDTO.getSuperior(), authorityMono);
        return authorityMono.flatMap(authorityRepository::save).flatMap(this::convertOuter);
    }

    /**
     * 设置上级
     *
     * @param superior      上级code
     * @param authorityMono 当前对象
     * @return 设置上级后的对象
     */
    private Mono<Authority> superior(String superior, Mono<Authority> authorityMono) {
        // 如果设置上级，进行处理
        if (StringUtils.hasText(superior)) {
            Mono<Authority> superiorMono = authorityRepository.getByCodeAndEnabledTrue(superior)
                    .switchIfEmpty(Mono.error(NoSuchElementException::new));

            return authorityMono.zipWith(superiorMono, (authority, sup) -> {
                authority.setSuperior(sup.getId());
                return authority;
            });
        }
        return authorityMono;
    }

    /**
     * 对象转换为输出结果对象(单条查询)
     *
     * @param authority 信息
     * @return 输出转换后的vo对象
     */
    private Mono<AuthorityVO> fetchOuter(Authority authority) {
        Mono<AuthorityVO> voMono = Mono.just(authority).map(a -> {
            AuthorityVO authorityVO = new AuthorityVO();
            BeanUtils.copyProperties(authority, authorityVO);
            return authorityVO;
        });

        if (authority.getSuperior() != null) {
            Mono<Authority> superiorMono = authorityRepository.findById(authority.getSuperior());
            voMono = voMono.zipWith(superiorMono, (a, superior) -> {
                a.setSuperior(superior.getCode());
                return a;
            });
        }
        return voMono;
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param authority 信息
     * @return 输出转换后的vo对象
     */
    private Mono<AuthorityVO> convertOuter(Authority authority) {
        Mono<AuthorityVO> voMono = Mono.just(authority).map(a -> {
            AuthorityVO authorityVO = new AuthorityVO();
            BeanUtils.copyProperties(authority, authorityVO);
            return authorityVO;
        });

        Mono<Long> longMono = roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(authority.getId())
                .switchIfEmpty(Mono.just(0L));
        voMono = voMono.zipWith(longMono, (a, count) -> {
            a.setCount(count);
            return a;
        });

        if (authority.getSuperior() != null) {
            Mono<Authority> superiorMono = authorityRepository.findById(authority.getSuperior());
            voMono = voMono.zipWith(superiorMono, (a, superior) -> {
                a.setSuperior(superior.getName());
                return a;
            });
        }
        return voMono;
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
        Map<String, String> expand = new HashMap<>();
        expand.put("icon", authority.getIcon());
        expand.put("path", authority.getPath());
        treeNode.setExpand(expand);
        return treeNode;
    }

}
