/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.hypervisor.dto.AuthorityDTO;
import top.abeille.basic.hypervisor.entity.Authority;
import top.abeille.basic.hypervisor.repository.AuthorityRepository;
import top.abeille.basic.hypervisor.service.AuthorityService;
import top.abeille.basic.hypervisor.vo.AuthorityVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.Collections;
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
    public Page<AuthorityVO> retrieves(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Authority> infoPage = authorityRepository.findAll(pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(this::convertOuter);
    }

    @Override
    public List<Authority> findByIdIn(List<Long> idList) {
        return authorityRepository.findByIdIn(idList);
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
