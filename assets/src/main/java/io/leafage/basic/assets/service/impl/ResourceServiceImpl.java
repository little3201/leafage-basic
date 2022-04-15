package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.entity.Category;
import io.leafage.basic.assets.entity.Resource;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.ResourceRepository;
import io.leafage.basic.assets.service.ResourceService;
import io.leafage.basic.assets.vo.ResourceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.leafage.common.basic.AbstractBasicService;
import java.util.Optional;

/**
 * resource service impl.
 *
 * @author liwenqiang 2021/09/29 15:10
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
    public Page<ResourceVO> retrieve(int page, int size, String category) {
        if (StringUtils.hasText(category)) {
            Category cate = categoryRepository.getByCodeAndEnabledTrue(category);
            return resourceRepository.findByCategoryIdAndEnabledTrue(PageRequest.of(page, size), cate.getId()).map(this::convertOuter);
        }
        return resourceRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public ResourceVO fetch(String code) {
        Assert.hasText(code, MESSAGE);
        Resource resource = resourceRepository.getByCodeAndEnabledTrue(code);
        if (resource == null) {
            return null;
        }
        return this.convertOuter(resource);
    }

    @Override
    public boolean exist(String title) {
        Assert.hasText(title, "title is blank.");
        return resourceRepository.existsByTitle(title);
    }

    @Override
    public ResourceVO create(ResourceDTO resourceDTO) {
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceDTO, resource);
        resource.setCode(this.generateCode());
        resource = resourceRepository.saveAndFlush(resource);
        return this.convertOuter(resource);
    }

    @Override
    public ResourceVO modify(String code, ResourceDTO resourceDTO) {
        Assert.hasText(code, MESSAGE);
        Resource resource = resourceRepository.getByCodeAndEnabledTrue(code);
        if (resource == null) {
            return null;
        }
        BeanUtils.copyProperties(resourceDTO, resource);
        resource = resourceRepository.save(resource);
        return this.convertOuter(resource);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param resource 信息
     * @return 输出转换后的vo对象
     */
    private ResourceVO convertOuter(Resource resource) {
        ResourceVO vo = new ResourceVO();
        BeanUtils.copyProperties(resource, vo);
        // 转换分类
        Optional<Category> optional = categoryRepository.findById(resource.getCategoryId());
        optional.ifPresent(category -> vo.setCategory(category.getName()));
        return vo;
    }

}
