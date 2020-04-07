/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.constant.PrefixEnum;
import top.abeille.basic.assets.document.ResourceInfo;
import top.abeille.basic.assets.dto.ResourceDTO;
import top.abeille.basic.assets.repository.ResourceRepository;
import top.abeille.basic.assets.service.ResourceService;
import top.abeille.basic.assets.vo.ResourceVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 资源信息service实现
 *
 * @author liwenqiang 2020/2/24 11:59
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
        return resourceRepository.findAll(sort).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<ResourceVO> create(ResourceDTO resourceDTO) {
        ResourceInfo info = new ResourceInfo();
        BeanUtils.copyProperties(resourceDTO, info);
        info.setBusinessId(PrefixEnum.RS + this.generateId());
        info.setEnabled(Boolean.TRUE);
        info.setModifyTime(LocalDateTime.now());
        return resourceRepository.insert(info).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<ResourceVO> modify(String businessId, ResourceDTO resourceDTO) {
        return this.fetchInfo(businessId).flatMap(articleInfo -> {
            BeanUtils.copyProperties(resourceDTO, articleInfo);
            return resourceRepository.save(articleInfo).filter(Objects::nonNull).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        return this.fetchInfo(businessId).flatMap(article -> resourceRepository.deleteById(article.getId()));
    }

    @Override
    public Mono<ResourceVO> fetchByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).map(this::convertOuter);
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<ResourceInfo> fetchInfo(String businessId) {
        ResourceInfo info = new ResourceInfo();
        info.setBusinessId(businessId);
        info.setEnabled(Boolean.TRUE);
        return resourceRepository.findOne(Example.of(info));
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
