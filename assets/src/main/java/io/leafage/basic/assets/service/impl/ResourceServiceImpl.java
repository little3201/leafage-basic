package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.entity.Resource;
import io.leafage.basic.assets.repository.ResourceRepository;
import io.leafage.basic.assets.service.ResourceService;
import io.leafage.basic.assets.vo.ResourceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.leafage.common.basic.AbstractBasicService;

/**
 * resource service 实现
 *
 * @author liwenqiang 2021/09/29 15:10
 **/
@Service
public class ResourceServiceImpl extends AbstractBasicService implements ResourceService {

    private static final String MESSAGE = "code is blank.";

    private final ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public Page<ResourceVO> retrieve(int page, int size) {
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
        return vo;
    }

}
