/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.entity.Authority;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import io.leafage.common.basic.AbstractBasicService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 权限资源信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class AuthorityServiceImpl extends AbstractBasicService implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Page<AuthorityVO> retrieve(int page, int size, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(order) ? order : "modify_time"));
        return authorityRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public AuthorityVO create(AuthorityDTO authorityDTO) {
        Authority info = new Authority();
        BeanUtils.copyProperties(authorityDTO, info);
        info.setCode(this.generateCode());
        Authority authority = authorityRepository.save(info);
        return this.convertOuter(authority);
    }

    @Override
    public List<AuthorityVO> saveAll(List<AuthorityDTO> entities) {
        return null;
    }

    @Override
    public void remove(String code) {
        Authority authority = authorityRepository.findByCodeAndEnabledTrue(code);
        authorityRepository.deleteById(authority.getId());
    }

    /**
     * 转换对象
     *
     * @param info 基础对象
     * @return 结果对象
     */
    private AuthorityVO convertOuter(Authority info) {
        AuthorityVO authorityVO = new AuthorityVO();
        BeanUtils.copyProperties(info, authorityVO);
        return authorityVO;
    }

}
