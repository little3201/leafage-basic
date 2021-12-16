/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Resource;
import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.ResourceRepository;
import io.leafage.basic.assets.service.ResourceService;
import io.leafage.basic.assets.vo.ResourceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;
import javax.naming.NotContextException;

/**
 * resource service 实现
 *
 * @author liwenqiang 2020/2/24 11:59
 **/
@Service
public class ResourceServiceImpl extends AbstractBasicService implements ResourceService {

    private static final String MESSAGE = "code is blank.";

    private final ResourceRepository resourceRepository;
    private final CategoryRepository categoryRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository, CategoryRepository categoryRepository) {
        this.resourceRepository = resourceRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Flux<ResourceVO> retrieve(int page, int size, String sort) {
        return resourceRepository.findByEnabledTrue(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,
                StringUtils.hasText(sort) ? sort : "modifyTime"))).flatMap(this::convertOuter);
    }

    @Override
    public Mono<ResourceVO> fetch(String code) {
        Assert.hasText(code, MESSAGE);
        return resourceRepository.getByCodeAndEnabledTrue(code)
                .flatMap(resource -> categoryRepository.findById(resource.getCategoryId())
                        .map(category -> {
                            ResourceVO resourceVO = new ResourceVO();
                            BeanUtils.copyProperties(resource, resourceVO);
                            resourceVO.setCategory(category.getCode());
                            return resourceVO;
                        }));
    }

    @Override
    public Mono<Boolean> exist(String title) {
        Assert.hasText(title, "title is blank.");
        return resourceRepository.existsByTitle(title);
    }

    @Override
    public Mono<Long> count(String category) {
        if (StringUtils.hasText(category)) {
            return categoryRepository.getByCodeAndEnabledTrue(category).flatMap(ca ->
                    resourceRepository.countByCategoryIdAndEnabledTrue(ca.getId()));
        }
        return resourceRepository.count();
    }

    @Override
    public Mono<ResourceVO> create(ResourceDTO resourceDTO) {
        return categoryRepository.getByCodeAndEnabledTrue(resourceDTO.getCategory())
                .switchIfEmpty(Mono.error(NotContextException::new))
                .map(category -> {
                    Resource resource = new Resource();
                    BeanUtils.copyProperties(resourceDTO, resource);
                    resource.setCode(this.generateCode());
                    resource.setCategoryId(category.getId());
                    return resource;
                }).flatMap(resourceRepository::insert).flatMap(this::convertOuter);
    }

    @Override
    public Mono<ResourceVO> modify(String code, ResourceDTO resourceDTO) {
        Assert.hasText(code, MESSAGE);
        return resourceRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NotContextException::new))
                .doOnNext(resource -> BeanUtils.copyProperties(resourceDTO, resource))
                .flatMap(resource -> categoryRepository.getByCodeAndEnabledTrue(resourceDTO.getCategory())
                        .switchIfEmpty(Mono.error(NotContextException::new))
                        .map(category -> {
                            resource.setCategoryId(category.getId());
                            return resource;
                        }).flatMap(resourceRepository::save).flatMap(this::convertOuter));

    }

    @Override
    public Mono<Void> remove(String code) {
        Assert.hasText(code, MESSAGE);
        return resourceRepository.getByCodeAndEnabledTrue(code).flatMap(article -> resourceRepository.deleteById(article.getId()));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param resource 信息
     * @return 输出转换后的vo对象
     */
    private Mono<ResourceVO> convertOuter(Resource resource) {
        return Mono.just(resource).map(r -> {
            ResourceVO outer = new ResourceVO();
            BeanUtils.copyProperties(r, outer);
            return outer;
        }).flatMap(resourceVO -> categoryRepository.findById(resource.getCategoryId()).map(category -> {
            resourceVO.setCategory(category.getAlias());
            return resourceVO;
        }).switchIfEmpty(Mono.just(resourceVO)));
    }

}
