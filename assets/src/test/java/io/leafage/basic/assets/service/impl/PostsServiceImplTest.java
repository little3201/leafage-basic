/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;


import com.mongodb.client.result.UpdateResult;
import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.PostsContentService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Set;
import static org.mockito.BDDMockito.given;

/**
 * posts service测试
 *
 * @author liwenqiang 2019/9/19 9:27
 */
@ExtendWith(MockitoExtension.class)
class PostsServiceImplTest {

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostsContentService postsContentService;

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private PostsServiceImpl postsService;

    @Test
    void retrieve() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        posts.setCategoryId(new ObjectId());
        given(this.postsRepository.findByEnabledTrue()).willReturn(Flux.just(posts));

        given(this.categoryRepository.findById(posts.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(this.postsService.retrieve()).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_page() {
        Posts posts = new Posts();
        ObjectId categoryId = new ObjectId();
        posts.setCategoryId(categoryId);
        given(this.postsRepository.findByEnabledTrue(PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(posts));

        given(this.categoryRepository.findById(categoryId)).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(this.postsService.retrieve(0, 2, "id")).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_page_category() {
        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        Posts posts = new Posts();
        posts.setId(new ObjectId());
        posts.setCategoryId(category.getId());
        given(this.postsRepository.findByCategoryIdAndEnabledTrue(category.getId(), PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(posts));

        given(this.categoryRepository.findById(posts.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(this.postsService.retrieve(0, 2, "21213G0J2", "id"))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void fetchDetails() {
        Posts posts = new Posts();
        ObjectId id = new ObjectId();
        posts.setId(id);
        ObjectId categoryId = new ObjectId();
        posts.setCategoryId(categoryId);
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(posts));

        given(this.reactiveMongoTemplate.upsert(Query.query(Criteria.where("id").is(id)),
                new Update().inc("viewed", 1), Posts.class))
                .willReturn(Mono.just(Mockito.mock(UpdateResult.class)));

        given(this.categoryRepository.findById(categoryId)).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postsContentService.fetchByPostsId(id)).willReturn(Mono.just(Mockito.mock(PostsContent.class)));

        StepVerifier.create(this.postsService.details("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetchDetails_empty() {
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.empty());
        StepVerifier.create(this.postsService.details("21213G0J2")).verifyError();
    }

    @Test
    void fetch() {
        Posts posts = new Posts();
        ObjectId categoryId = new ObjectId();
        posts.setCategoryId(categoryId);
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(posts));

        given(this.categoryRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.fetch("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetchContent() {
        Posts posts = new Posts();
        ObjectId id = new ObjectId();
        posts.setId(id);
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(posts));

        given(this.postsContentService.fetchByPostsId(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(PostsContent.class)));

        StepVerifier.create(postsService.content("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        given(this.postsRepository.countByCategoryIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        StepVerifier.create(postsService.count("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void count_all() {
        given(this.postsRepository.count()).willReturn(Mono.just(2L));

        StepVerifier.create(postsService.count("")).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.postsRepository.existsByTitle(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(postsService.exist("test")).expectNext(Boolean.TRUE).verifyComplete();
    }

    @Test
    void create() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(Mockito.mock(Category.class)));

        Posts posts = new Posts();
        posts.setId(new ObjectId());
        posts.setCategoryId(new ObjectId());
        given(this.postsRepository.insert(Mockito.any(Posts.class))).willReturn(Mono.just(posts));

        given(this.postsContentService.create(Mockito.any(PostsContent.class))).willReturn(Mono.empty());
        
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setCategory("21213G0J2");
        StepVerifier.create(this.postsService.create(postsDTO)).verifyComplete();
    }

    @Test
    void create_error_null() {
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setCategory("21213G0J2");
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.empty());

        StepVerifier.create(this.postsService.create(postsDTO)).verifyError();
    }

    @Test
    void create_error() {
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setCategory("21213G0J2");
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postsRepository.insert(Mockito.any(Posts.class))).willThrow(new RuntimeException());

        StepVerifier.create(this.postsService.create(postsDTO)).verifyError();
    }

    @Test
    void modify() {
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        Posts posts = new Posts();
        posts.setId(new ObjectId());
        posts.setCategoryId(category.getId());
        given(this.postsRepository.save(Mockito.any(Posts.class))).willReturn(Mono.just(posts));

        PostsContent postsContent = new PostsContent();
        postsContent.setPostsId(posts.getId());
        given(this.postsContentService.fetchByPostsId(Mockito.any(ObjectId.class)))
                .willReturn(Mono.just(postsContent));

        postsContent.setContent("内容信息");
        given(this.postsContentService.modify(posts.getId(), postsContent))
                .willReturn(Mono.empty());

        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("标题");
        postsDTO.setTags(Set.of("test"));
        postsDTO.setCover("./avatar.jpg");
        postsDTO.setCategory("21213G0J2");
        postsDTO.setContent("内容信息");
        StepVerifier.create(this.postsService.modify("21213G0J2", postsDTO)).verifyComplete();
    }

    @Test
    void remove() {
        Posts posts = new Posts();
        ObjectId id = new ObjectId();
        posts.setId(id);
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(posts));

        given(this.reactiveMongoTemplate.upsert(Query.query(Criteria.where("id").is(id)),
                Update.update("enabled", false), Posts.class)).willReturn(Mono.just(Mockito.mock(UpdateResult.class)));

        StepVerifier.create(postsService.remove("21213G0J2")).verifyComplete();
    }

    @Test
    void next() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(posts));

        posts.setCategoryId(new ObjectId());
        given(this.postsRepository.findByIdGreaterThanAndEnabledTrue(Mockito.any(ObjectId.class),
                Mockito.any(PageRequest.class))).willReturn(Flux.just(posts));

        given(this.categoryRepository.findById(posts.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.next("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void previous() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(posts));

        posts.setCategoryId(new ObjectId());
        given(this.postsRepository.findByIdLessThanAndEnabledTrue(Mockito.any(ObjectId.class),
                Mockito.any(PageRequest.class))).willReturn(Flux.just(posts));

        given(this.categoryRepository.findById(posts.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.previous("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void likes() {
        given(this.reactiveMongoTemplate.upsert(Query.query(Criteria.where("code").is("21213G0J2")),
                new Update().inc("likes", 1), Posts.class)).willReturn(Mono.just(Mockito.mock(UpdateResult.class)));

        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        StepVerifier.create(postsService.like("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void search() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        posts.setCategoryId(new ObjectId());
        given(this.postsRepository.findByTitleIgnoreCaseLikeAndEnabledTrue(Mockito.anyString())).willReturn(Flux.just(posts));

        given(this.categoryRepository.findById(posts.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.search("leafag")).expectNextCount(1).verifyComplete();
    }

}