package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.Category;
import io.leafage.basic.assets.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * resource 接口测试
 *
 * @author liwenqiang 2021/12/7 17:55
 **/
@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    @Test
    void retrieve() {
        Resource resource = new Resource();
        resource.setTitle("java");
        resource.setType('E');
        resource.setViewed(234);
        resource.setDownloads(234);
        Page<Resource> page = new PageImpl<>(List.of(resource));
        given(this.resourceRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(page);

        Page<ResourceVO> voPage = resourceService.retrieve(0, 2, "");
        Assertions.assertNotNull(voPage.getContent());
    }


    @Test
    void retrieve_category() {
        Category category = new Category();
        category.setId(1L);
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(category);

        Resource resource = new Resource();
        resource.setTitle("java");
        resource.setCategoryId(category.getId());

        Resource rs = new Resource();
        rs.setTitle("java");
        rs.setCategoryId(resource.getCategoryId());
        Page<Resource> page = new PageImpl<>(List.of(resource, rs));
        given(this.resourceRepository.findByCategoryIdAndEnabledTrue(PageRequest.of(0, 2), category.getId())).willReturn(page);

        Page<ResourceVO> voPage = resourceService.retrieve(0, 2, "21389KO6");
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        Resource resource = new Resource();
        resource.setTitle("java");
        given(this.resourceRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(resource);

        ResourceVO resourceVO = resourceService.fetch("21389KO6");
        Assertions.assertNotNull(resourceVO);
    }

    @Test
    void fetch_null() {
        given(this.resourceRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(null);

        ResourceVO resourceVO = resourceService.fetch("21389KO6");
        Assertions.assertNull(resourceVO);
    }

    @Test
    void exist() {
        given(this.resourceRepository.existsByTitle(Mockito.anyString())).willReturn(true);

        boolean exist = resourceService.exist("java");
        Assertions.assertTrue(exist);
    }

    @Test
    void create() {
        Resource resource = new Resource();
        resource.setTitle("java");
        given(this.resourceRepository.saveAndFlush(Mockito.any(Resource.class))).willReturn(resource);

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("java");
        resourceDTO.setCover("/avatar.jpg");
        resourceDTO.setType('E');
        resourceDTO.setDescription("描述");
        ResourceVO resourceVO = resourceService.create(resourceDTO);

        verify(this.resourceRepository, times(1)).saveAndFlush(Mockito.any(Resource.class));
        Assertions.assertNotNull(resourceVO);
    }

    @Test
    void modify() {
        Resource resource = new Resource();
        resource.setTitle("java");
        given(this.resourceRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(resource);

        given(this.resourceRepository.save(Mockito.any(Resource.class))).willReturn(Mockito.mock(Resource.class));

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("java");
        resourceDTO.setCover("/avatar.jpg");
        resourceDTO.setType('E');
        resourceDTO.setDescription("描述");
        ResourceVO resourceVO = resourceService.modify("21389KO6", resourceDTO);

        verify(this.resourceRepository, times(1)).save(Mockito.any(Resource.class));
        Assertions.assertNotNull(resourceVO);
    }

    @Test
    void modify_null() {
        given(this.resourceRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(null);

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("java");
        resourceDTO.setCover("/avatar.jpg");
        resourceDTO.setType('E');
        resourceDTO.setDescription("描述");
        ResourceVO resourceVO = resourceService.modify("21389KO6", resourceDTO);
        Assertions.assertNull(resourceVO);
    }
}