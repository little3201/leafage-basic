/*
 *  Copyright 2018-2023 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.bo.CategoryBO;
import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.document.Resource;
import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.ResourceRepository;
import io.leafage.basic.assets.service.ResourceService;
import io.leafage.basic.assets.vo.ResourceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.AbstractBasicService;
import top.leafage.common.ValidMessage;

import javax.naming.NotContextException;

/**
 * resource service impl
 *
 * @author liwenqiang 2020-02-24 11:59
 **/
@Service
public class ResourceServiceImpl extends AbstractBasicService implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final CategoryRepository categoryRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository, CategoryRepository categoryRepository) {
        this.resourceRepository = resourceRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Mono<Page<ResourceVO>> retrieve(int page, int size, String sort, String category) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, StringUtils.hasText(sort) ? sort : "modifyTime");
        Flux<ResourceVO> voFlux;
        Mono<Long> count;

        if (!StringUtils.hasText(category)) {
            voFlux = resourceRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuter);

            count = resourceRepository.countByEnabledTrue();
        } else {
            Mono<Category> categoryMono = categoryRepository.getByCodeAndEnabledTrue(category);
            voFlux = categoryMono.flatMapMany(c -> resourceRepository.findByCategoryIdAndEnabledTrue(c.getId(), pageRequest)
                    .flatMap(this::convertOuter));

            count = categoryMono.flatMap(c -> resourceRepository.countByCategoryIdAndEnabledTrue(c.getId()));
        }

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<ResourceVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return resourceRepository.getByCodeAndEnabledTrue(code)
                .flatMap(resource -> categoryRepository.findById(resource.getCategoryId())
                        .map(category -> {
                            ResourceVO resourceVO = new ResourceVO();
                            BeanUtils.copyProperties(resource, resourceVO);
                            // 转换category
                            CategoryBO basicVO = new CategoryBO();
                            BeanUtils.copyProperties(category, basicVO);
                            resourceVO.setCategory(basicVO);
                            return resourceVO;
                        }));
    }

    @Override
    public Mono<Boolean> exist(String title) {
        Assert.hasText(title, "title is blank.");
        return resourceRepository.existsByTitle(title);
    }

    @Override
    public Mono<ResourceVO> create(ResourceDTO resourceDTO) {
        return categoryRepository.getByCodeAndEnabledTrue(resourceDTO.getCategory().getCode())
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
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return resourceRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NotContextException::new))
                .doOnNext(resource -> BeanUtils.copyProperties(resourceDTO, resource))
                .flatMap(resource -> categoryRepository.getByCodeAndEnabledTrue(resourceDTO.getCategory().getCode())
                        .switchIfEmpty(Mono.error(NotContextException::new))
                        .map(category -> {
                            resource.setCategoryId(category.getId());
                            return resource;
                        }).flatMap(resourceRepository::save).flatMap(this::convertOuter));

    }

    @Override
    public Mono<Void> remove(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
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
            // 转换category
            CategoryBO basicVO = new CategoryBO();
            BeanUtils.copyProperties(category, basicVO);
            resourceVO.setCategory(basicVO);
            return resourceVO;
        }).switchIfEmpty(Mono.just(resourceVO)));
    }

}
