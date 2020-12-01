/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.constant.ResourceTypeEnum;
import top.abeille.basic.hypervisor.document.AuthorityInfo;
import top.abeille.basic.hypervisor.dto.AuthorityDTO;
import top.abeille.basic.hypervisor.repository.AuthorityRepository;
import top.abeille.basic.hypervisor.service.AuthorityService;
import top.abeille.basic.hypervisor.vo.AuthorityVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
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
    public Flux<AuthorityVO> retrieveAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return authorityRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<AuthorityVO> fetchByCode(String code) {
        Asserts.notBlank(code, "code");
        return this.findByCodeAndEnabledTrue(code).map(this::convertOuter);
    }

    @Override
    public Mono<AuthorityVO> create(AuthorityDTO authorityDTO) {
        AuthorityInfo info = new AuthorityInfo();
        BeanUtils.copyProperties(authorityDTO, info);
        String prefix;
        switch (ResourceTypeEnum.valueOf(authorityDTO.getType())) {
            // 菜单
            case MENU:
                prefix = PrefixEnum.RM.name();
                break;
            // 按钮
            case BUTTON:
                prefix = PrefixEnum.RB.name();
                break;
            // tab页
            case TAB:
                prefix = PrefixEnum.RT.name();
                break;
            // 路径/接口
            default:
                prefix = PrefixEnum.RU.name();
        }
        info.setCode(prefix + this.generateId());
        info.setModifyTime(LocalDateTime.now());
        return authorityRepository.insert(info).map(this::convertOuter);
    }

    @Override
    public Mono<AuthorityVO> modify(String code, AuthorityDTO authorityDTO) {
        Asserts.notBlank(code, "code");
        return this.findByCodeAndEnabledTrue(code).flatMap(info -> {
            BeanUtils.copyProperties(authorityDTO, info);
            info.setModifyTime(LocalDateTime.now());
            return authorityRepository.save(info);
        }).map(this::convertOuter);
    }

    @Override
    public Mono<AuthorityInfo> findByCodeAndEnabledTrue(String code) {
        Asserts.notBlank(code, "code");
        return authorityRepository.findByCodeAndEnabledTrue(code);
    }

    @Override
    public Flux<AuthorityInfo> findByIdInAndEnabledTrue(List<String> sourceIdList) {
        Asserts.notNull(sourceIdList, "sourceIdList");
        return authorityRepository.findByIdInAndEnabledTrue(sourceIdList);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private AuthorityVO convertOuter(AuthorityInfo info) {
        AuthorityVO outer = new AuthorityVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}