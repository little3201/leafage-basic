/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.Category;
import top.abeille.basic.assets.dto.CategoryDTO;
import top.abeille.basic.assets.repository.CategoryRepository;
import top.abeille.basic.assets.repository.PostsRepository;
import top.abeille.basic.assets.service.CategoryService;
import top.abeille.basic.assets.vo.CategoryVO;
import top.abeille.basic.assets.vo.CountVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.Set;

/**
 * 话题信息service实现
 *
 * @author liwenqiang 2020/2/13 20:24
 **/
@Service
public class CategoryServiceImpl extends AbstractBasicService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostsRepository postsRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, PostsRepository postsRepository) {
        this.categoryRepository = categoryRepository;
        this.postsRepository = postsRepository;
    }

    @Override
    public Flux<CategoryVO> retrieve(int page, int size) {
        return categoryRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public Mono<CategoryVO> fetch(String code) {
        Asserts.notBlank(code, "code");
        Category info = new Category();
        info.setCode(code);
        return categoryRepository.findByCodeAndEnabledTrue(code).map(this::convertOuter);
    }

    @Override
    public Flux<CountVO> countPosts(Set<String> codes) {
        return Flux.fromIterable(codes)
                .flatMap(categoryRepository::findByCodeAndEnabledTrue)
                .flatMap(category -> postsRepository.countByCategoryIdAndEnabledTrue(category.getId())
                        .map(count -> {
                            CountVO countVO = new CountVO();
                            countVO.setCode(category.getCode());
                            countVO.setCount(count);
                            return countVO;
                        })
                );
    }

    @Override
    public Mono<CategoryVO> create(CategoryDTO categoryDTO) {
        Category info = new Category();
        BeanUtils.copyProperties(categoryDTO, info);
        info.setCode(this.generateCode());
        return categoryRepository.insert(info).map(this::convertOuter);
    }

    @Override
    public Mono<CategoryVO> modify(String code, CategoryDTO categoryDTO) {
        Asserts.notBlank(code, "code");
        return categoryRepository.findByCodeAndEnabledTrue(code).flatMap(category -> {
            BeanUtils.copyProperties(categoryDTO, category);
            return categoryRepository.save(category).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> remove(String code) {
        Asserts.notBlank(code, "code");
        return categoryRepository.findByCodeAndEnabledTrue(code).flatMap(topic -> categoryRepository.deleteById(topic.getId()));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private CategoryVO convertOuter(Category info) {
        CategoryVO outer = new CategoryVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
