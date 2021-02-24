/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import io.leafage.common.basic.AbstractBasicService;
import org.apache.http.util.Asserts;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 权限资源信息Service实现
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
    public Flux<AuthorityVO> retrieve(int page, int size) {
        return authorityRepository.findByEnabledTrue(PageRequest.of(page, size))
                .flatMap(authority -> roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(authority.getId())
                        .map(count -> {
                            AuthorityVO authorityVO = new AuthorityVO();
                            BeanUtils.copyProperties(authority, authorityVO);
                            authorityVO.setCount(count);
                            return authorityVO;
                        })
                )
                .flatMap(authorityVO -> {
                    if (StringUtils.hasText(authorityVO.getSuperior())) {
                        return authorityRepository.getById(new ObjectId(authorityVO.getSuperior()))
                                .map(superior -> {
                                            authorityVO.setSuperior(superior.getName());
                                            return authorityVO;
                                        }
                                );
                    }
                    return Mono.just(authorityVO);
                });
    }

    @Override
    public Mono<AuthorityVO> fetch(String code) {
        Asserts.notBlank(code, "code");
        return authorityRepository.getByCodeAndEnabledTrue(code)
                .flatMap(authority -> roleAuthorityRepository.countByAuthorityIdAndEnabledTrue(authority.getId())
                        .map(count -> {
                            AuthorityVO authorityVO = new AuthorityVO();
                            BeanUtils.copyProperties(authority, authorityVO);
                            authorityVO.setCount(count);
                            return authorityVO;
                        })
                );
    }

    @Override
    public Mono<AuthorityVO> create(AuthorityDTO authorityDTO) {
        Authority info = new Authority();
        BeanUtils.copyProperties(authorityDTO, info);
        info.setCode(this.generateCode());
        return authorityRepository.insert(info).map(this::convertOuter);
    }

    @Override
    public Mono<AuthorityVO> modify(String code, AuthorityDTO authorityDTO) {
        Asserts.notBlank(code, "code");
        return authorityRepository.getByCodeAndEnabledTrue(code).flatMap(info -> {
            BeanUtils.copyProperties(authorityDTO, info);
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
}
