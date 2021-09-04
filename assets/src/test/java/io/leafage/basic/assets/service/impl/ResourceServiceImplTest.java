/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Resource;
import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.repository.ResourceRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.BDDMockito.given;

/**
 * portfolio service测试
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceServiceImpl portfolioService;

    @Test
    void retrieve() {
        given(this.resourceRepository.findByEnabledTrue(PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(Mockito.mock(Resource.class)));
        StepVerifier.create(this.portfolioService.retrieve(0, 2, "id")).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Mockito.when(this.resourceRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .thenReturn(Mono.just(Mockito.mock(Resource.class)));
        StepVerifier.create(portfolioService.fetch("21318H9FH")).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        given(this.resourceRepository.count()).willReturn(Mono.just(2L));
        StepVerifier.create(portfolioService.count()).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.resourceRepository.existsByTitle(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(portfolioService.exist("test")).expectNext(Boolean.TRUE).verifyComplete();
    }

    @Test
    void create() {
        Resource resource = new Resource();
        resource.setId(new ObjectId());
        resource.setType("jpg");
        resource.setCode("21318H9FH");
        resource.setTitle("test");
        resource.setComment(1);
        resource.setLikes(23);
        resource.setViewed(232);
        given(this.resourceRepository.insert(Mockito.any(Resource.class))).willReturn(Mono.just(resource));

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        StepVerifier.create(portfolioService.create(resourceDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        Resource resource = new Resource();
        resource.setId(new ObjectId());
        resource.setType("jpg");
        resource.setCode("21318H9FH");
        resource.setTitle("test");
        resource.setComment(1);
        resource.setLikes(23);
        resource.setViewed(232);
        given(this.resourceRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(resource));

        given(this.resourceRepository.save(Mockito.any(Resource.class))).willReturn(Mono.just(Mockito.mock(Resource.class)));

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        StepVerifier.create(portfolioService.modify("21318H9FH", resourceDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        Resource resource = new Resource();
        resource.setId(new ObjectId());
        given(this.resourceRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(resource));

        given(this.resourceRepository.deleteById(Mockito.any(ObjectId.class))).willReturn(Mono.empty());

        StepVerifier.create(portfolioService.remove("21318H9FH")).verifyComplete();
    }
}