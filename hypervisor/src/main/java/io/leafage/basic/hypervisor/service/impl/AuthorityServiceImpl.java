/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.domain.TreeNode;
import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;

import javax.naming.NotContextException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Authority authority = new Authority();
        authority.setType(type);
        authority.setEnabled(true);
        return authorityRepository.findAll(Example.of(authority)).map(this::convertOuter);
    }

    @Override
    public Flux<AuthorityVO> retrieve(int page, int size) {
        return authorityRepository.findByEnabledTrue(PageRequest.of(page, size))
                .flatMap(authority -> roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(authority.getId())
                        .flatMap(count -> {
                            AuthorityVO authorityVO = new AuthorityVO();
                            BeanUtils.copyProperties(authority, authorityVO);
                            authorityVO.setCount(count);
                            if (authority.getSuperior() != null) {
                                return authorityRepository.getById(authority.getSuperior())
                                        .map(superior -> {
                                                    authorityVO.setSuperior(superior.getName());
                                                    return authorityVO;
                                                }
                                        );
                            }
                            return Mono.just(authorityVO);
                        })
                );
    }

    @Override
    public Flux<TreeNode> tree() {
        Authority authority = new Authority();
        authority.setType('M');
        authority.setEnabled(true);
        Flux<Authority> authorities = authorityRepository.findAll(Example.of(authority));
        return authorities.filter(a -> a.getSuperior() == null).map(a -> {
            TreeNode treeNode = this.constructNode(a.getCode(), a);
            this.addChildren(authority, authorities);
            return treeNode;
        });
    }

    @Override
    public Mono<AuthorityVO> fetch(String code) {
        Assert.hasText(code, "code is blank");
        return authorityRepository.getByCodeAndEnabledTrue(code)
                .flatMap(authority -> roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(authority.getId())
                        .flatMap(count -> {
                            AuthorityVO authorityVO = new AuthorityVO();
                            BeanUtils.copyProperties(authority, authorityVO);
                            authorityVO.setCount(count);
                            if (authority.getSuperior() != null) {
                                return authorityRepository.getById(authority.getSuperior())
                                        .map(superior -> {
                                                    authorityVO.setSuperior(superior.getCode());
                                                    return authorityVO;
                                                }
                                        );
                            }
                            return Mono.just(authorityVO);
                        })
                );
    }

    @Override
    public Mono<Long> count() {
        return authorityRepository.count();
    }

    @Override
    public Mono<AuthorityVO> create(AuthorityDTO authorityDTO) {
        Authority info = new Authority();
        BeanUtils.copyProperties(authorityDTO, info);
        info.setCode(this.generateCode());
        Mono<Authority> authorityMono;
        // 如果设置上级，进行处理
        if (StringUtils.hasText(authorityDTO.getSuperior())) {
            authorityMono = authorityRepository.getByCodeAndEnabledTrue(authorityDTO.getSuperior())
                    .switchIfEmpty(Mono.error(NotContextException::new))
                    .doOnNext(superior -> info.setSuperior(superior.getId()));
        } else {
            authorityMono = Mono.just(info);
        }
        return authorityMono.flatMap(authorityRepository::insert).map(this::convertOuter);
    }

    @Override
    public Mono<AuthorityVO> modify(String code, AuthorityDTO authorityDTO) {
        Assert.hasText(code, "code is blank");
        return authorityRepository.getByCodeAndEnabledTrue(code).flatMap(info -> {
            BeanUtils.copyProperties(authorityDTO, info);
            // 判断是否设置上级
            if (StringUtils.hasText(authorityDTO.getSuperior())) {
                authorityRepository.getByCodeAndEnabledTrue(authorityDTO.getSuperior())
                        .switchIfEmpty(Mono.error(NotContextException::new))
                        .doOnNext(superior -> info.setSuperior(superior.getId())).then();
            }
            return authorityRepository.save(info);
        }).map(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private AuthorityVO convertOuter(Authority info) {
        AuthorityVO outer = new AuthorityVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }

    /**
     * construct tree node
     *
     * @param superior  superior code
     * @param authority data
     * @return tree node
     */
    private TreeNode constructNode(String superior, Authority authority) {
        TreeNode treeNode = new TreeNode();
        treeNode.setCode(authority.getCode());
        treeNode.setName(authority.getName());
        treeNode.setSuperior(superior);
        Map<String, String> expand = new HashMap<>();
        expand.put("icon", authority.getIcon());
        expand.put("path", authority.getPath());
        treeNode.setExpand(expand);
        return treeNode;
    }

    /**
     * add child node
     *
     * @param superior    superior node
     * @param authorities to be build source data
     * @return tree node
     */
    private List<TreeNode> addChildren(Authority superior, Flux<Authority> authorities) {
        authorities.filter(authority -> superior.getId().equals(authority.getSuperior())).doOnEach(authoritySignal ->
                authoritySignal.get());


//        return authorities.filter(authority -> superior.getId().equals(authority.getSuperior())).map(authority -> {
//            TreeNode treeNode = this.constructNode(superior.getCode(), authority);
//            return this.addChildren(authority, authorities).map(treeNodes -> {
//                treeNode.setChildren(treeNodes);
//                return treeNode;
//            });
//        });
        return null;
    }

}
