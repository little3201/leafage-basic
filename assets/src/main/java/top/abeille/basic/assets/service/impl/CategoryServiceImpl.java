/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.constant.PrefixEnum;
import top.abeille.basic.assets.document.CategoryInfo;
import top.abeille.basic.assets.dto.CategoryDTO;
import top.abeille.basic.assets.repository.CategoryRepository;
import top.abeille.basic.assets.service.CategoryService;
import top.abeille.basic.assets.vo.CategoryVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;

/**
 * 话题信息service实现
 *
 * @author liwenqiang 2020/2/13 20:24
 **/
@Service
public class CategoryServiceImpl extends AbstractBasicService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Flux<CategoryVO> retrieveAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return categoryRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<CategoryVO> fetchByCode(String code) {
        Asserts.notBlank(code, "code");
        CategoryInfo info = new CategoryInfo();
        info.setCode(code);
        return categoryRepository.findByCodeAndEnabledTrue(code).map(this::convertOuter);
    }

    @Override
    public Mono<CategoryVO> create(CategoryDTO categoryDTO) {
        CategoryInfo info = new CategoryInfo();
        BeanUtils.copyProperties(categoryDTO, info);
        info.setCode(PrefixEnum.CG + this.generateId());
        info.setModifyTime(LocalDateTime.now());
        return categoryRepository.insert(info).map(this::convertOuter);
    }

    @Override
    public Mono<CategoryVO> modify(String code, CategoryDTO categoryDTO) {
        Asserts.notBlank(code, "code");
        return categoryRepository.findByCodeAndEnabledTrue(code).flatMap(categoryInfo -> {
            BeanUtils.copyProperties(categoryDTO, categoryInfo);
            categoryInfo.setModifyTime(LocalDateTime.now());
            return categoryRepository.save(categoryInfo).map(this::convertOuter);
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
    private CategoryVO convertOuter(CategoryInfo info) {
        CategoryVO outer = new CategoryVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
