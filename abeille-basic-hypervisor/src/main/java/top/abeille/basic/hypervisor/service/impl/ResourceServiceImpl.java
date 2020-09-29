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
import top.abeille.basic.hypervisor.constant.SourceTypeEnum;
import top.abeille.basic.hypervisor.document.ResourceInfo;
import top.abeille.basic.hypervisor.dto.SourceDTO;
import top.abeille.basic.hypervisor.repository.ResourceRepository;
import top.abeille.basic.hypervisor.service.ResourceService;
import top.abeille.basic.hypervisor.vo.ResourceVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限资源信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class ResourceServiceImpl extends AbstractBasicService implements ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public Flux<ResourceVO> retrieveAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return resourceRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<ResourceVO> fetchByCode(String code) {
        Asserts.notBlank(code, "code");
        return this.findByCodeAndEnabledTrue(code).map(this::convertOuter);
    }

    @Override
    public Mono<ResourceVO> create(SourceDTO sourceDTO) {
        ResourceInfo info = new ResourceInfo();
        BeanUtils.copyProperties(sourceDTO, info);
        String prefix;
        switch (SourceTypeEnum.valueOf(sourceDTO.getType())) {
            case MENU:
                prefix = PrefixEnum.SM.name(); // 菜单
                break;
            case BUTTON:
                prefix = PrefixEnum.SB.name(); // 按钮
                break;
            case TAB:
                prefix = PrefixEnum.ST.name(); // tab页
                break;
            default:
                prefix = PrefixEnum.SU.name(); // 路径
        }
        info.setCode(prefix + this.generateId());
        info.setEnabled(true);
        info.setModifyTime(LocalDateTime.now());
        return resourceRepository.insert(info).map(this::convertOuter);
    }

    @Override
    public Mono<ResourceVO> modify(String code, SourceDTO sourceDTO) {
        Asserts.notBlank(code, "code");
        return this.findByCodeAndEnabledTrue(code).flatMap(info -> {
            BeanUtils.copyProperties(sourceDTO, info);
            info.setModifyTime(LocalDateTime.now());
            return resourceRepository.save(info);
        }).map(this::convertOuter);
    }

    @Override
    public Mono<ResourceInfo> findByCodeAndEnabledTrue(String code) {
        Asserts.notBlank(code, "code");
        return resourceRepository.findByCodeAndEnabledTrue(code);
    }

    @Override
    public Flux<ResourceInfo> findByIdInAndEnabledTrue(List<String> sourceIdList) {
        Asserts.notNull(sourceIdList, "sourceIdList");
        return resourceRepository.findByIdInAndEnabledTrue(sourceIdList);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private ResourceVO convertOuter(ResourceInfo info) {
        ResourceVO outer = new ResourceVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
