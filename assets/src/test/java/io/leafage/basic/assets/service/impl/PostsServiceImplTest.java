/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;


import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.entity.Category;
import io.leafage.basic.assets.entity.Posts;
import io.leafage.basic.assets.entity.PostsContent;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsContentRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.vo.ContentVO;
import io.leafage.basic.assets.vo.PostsVO;
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
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * 帖子接口测试
 *
 * @author liwenqiang 2019-08-20 22:38
 **/
@ExtendWith(MockitoExtension.class)
class PostsServiceImplTest {

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private PostsContentRepository postsContentRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private PostsServiceImpl postsService;

    @Test
    void retrieve_page() {
        Posts posts = new Posts();
        posts.setTitle("test");
        posts.setTags("java,spring");
        Page<Posts> postsPage = new PageImpl<>(List.of(posts));
        given(this.postsRepository.findByEnabledTrue(PageRequest.of(0, 2, Sort.by("id")))).willReturn(postsPage);

        Page<PostsVO> voPage = postsService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Posts.class));

        PostsVO postsVO = postsService.fetch("2112JK02");

        Assertions.assertNotNull(postsVO);
    }

    @Test
    void details() {
        Posts posts = new Posts();
        posts.setId(12L);
        posts.setTags("测试,test");
        posts.setCategoryId(2L);
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(posts);

        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Category.class)));

        PostsContent postsContent = new PostsContent();
        postsContent.setContent("测试内容");
        postsContent.setPostsId(posts.getId());
        postsContent.setCatalog("目录");
        given(this.postsContentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(postsContent);

        PostsVO postsVO = postsService.details("2112JK02");

        Assertions.assertNotNull(postsVO);
    }

    @Test
    void content_posts_null() {
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(null);

        ContentVO contentVO = postsService.content("2112JK02");

        Assertions.assertNull(contentVO);
    }

    @Test
    void content() {
        Posts posts = new Posts();
        posts.setId(12L);
        posts.setTags("测试,test");
        posts.setCategoryId(2L);
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(posts);

        PostsContent postsContent = new PostsContent();
        postsContent.setContent("测试内容");
        postsContent.setPostsId(posts.getId());
        postsContent.setCatalog("目录");
        given(this.postsContentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(postsContent);

        ContentVO contentVO = postsService.content("2112JK02");

        Assertions.assertNotNull(contentVO);
    }

    @Test
    void exist() {
        given(this.postsRepository.existsByTitle(Mockito.anyString())).willReturn(true);

        boolean exist = postsService.exist("spring");

        Assertions.assertTrue(exist);
    }

    @Test
    void create_content_null() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Category.class));

        Posts posts = new Posts();
        posts.setId(12L);
        posts.setTags("测试,test");
        given(this.postsRepository.saveAndFlush(Mockito.any(Posts.class))).willReturn(posts);

        PostsContent postsContent = new PostsContent();
        postsContent.setContent("测试内容");
        postsContent.setPostsId(posts.getId());
        postsContent.setCatalog("目录");
        given(this.postsContentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(null);

        given(this.postsContentRepository.saveAndFlush(Mockito.any(PostsContent.class))).willReturn(postsContent);

        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setCategory("2112JOP2");
        postsDTO.setTags(Set.of(posts.getTags().split(",")));
        PostsVO postsVO = postsService.create(postsDTO);

        posts.setId(postsContent.getPostsId());
        verify(this.postsRepository, times(1)).saveAndFlush(Mockito.any(Posts.class));
        verify(this.postsContentRepository, times(1)).saveAndFlush(Mockito.any(PostsContent.class));
        Assertions.assertNotNull(postsVO);
    }

    @Test
    void create() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Category.class));

        Posts posts = new Posts();
        posts.setId(12L);
        posts.setTags("测试,test");
        given(this.postsRepository.saveAndFlush(Mockito.any(Posts.class))).willReturn(posts);

        PostsContent postsContent = new PostsContent();
        postsContent.setContent("测试内容");
        postsContent.setPostsId(posts.getId());
        postsContent.setCatalog("目录");
        given(this.postsContentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(postsContent);

        given(this.postsContentRepository.saveAndFlush(Mockito.any(PostsContent.class))).willReturn(postsContent);

        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setCategory("2112JOP2");
        postsDTO.setTags(Set.of(posts.getTags().split(",")));
        PostsVO postsVO = postsService.create(postsDTO);

        posts.setId(postsContent.getPostsId());
        verify(this.postsRepository, times(1)).saveAndFlush(Mockito.any(Posts.class));
        verify(this.postsContentRepository, times(1)).saveAndFlush(Mockito.any(PostsContent.class));
        Assertions.assertNotNull(postsVO);
    }

    @Test
    void modify_category_null() {
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Posts.class));

        given(this.postsRepository.save(Mockito.any(Posts.class))).willReturn(Mockito.mock(Posts.class));

        given(this.postsContentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(PostsContent.class));

        given(this.postsContentRepository.save(Mockito.any(PostsContent.class))).willReturn(Mockito.mock(PostsContent.class));

        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setCategory("2112JOP2");
        PostsVO postsVO = postsService.modify("2112JK02", postsDTO);

        verify(this.postsRepository, times(1)).save(Mockito.any(Posts.class));
        verify(this.postsContentRepository, times(1)).save(Mockito.any(PostsContent.class));
        Assertions.assertNotNull(postsVO);
    }

    @Test
    void modify_posts_null() {
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(null);

        PostsVO postsVO = postsService.modify("2112JK02", Mockito.mock(PostsDTO.class));

        Assertions.assertNull(postsVO);
    }


    @Test
    void modify() {
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Posts.class));

        Category category = new Category();
        category.setId(1L);
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(category);

        given(this.postsRepository.save(Mockito.any(Posts.class))).willReturn(Mockito.mock(Posts.class));

        given(this.postsContentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(PostsContent.class));

        given(this.postsContentRepository.save(Mockito.any(PostsContent.class))).willReturn(Mockito.mock(PostsContent.class));

        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setCategory("2112JOP2");
        PostsVO postsVO = postsService.modify("2112JK02", postsDTO);

        verify(this.postsRepository, times(1)).save(Mockito.any(Posts.class));
        verify(this.postsContentRepository, times(1)).save(Mockito.any(PostsContent.class));
        Assertions.assertNotNull(postsVO);
    }

    @Test
    void modify_error() {
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Posts.class));

        given(this.postsRepository.save(Mockito.any(Posts.class))).willReturn(Mockito.mock(Posts.class));

        given(this.postsContentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(null);

        given(this.postsContentRepository.save(Mockito.any(PostsContent.class))).willReturn(Mockito.mock(PostsContent.class));

        PostsVO postsVO = postsService.modify("2112JK02", Mockito.mock(PostsDTO.class));

        verify(this.postsRepository, times(1)).save(Mockito.any(Posts.class));
        verify(this.postsContentRepository, times(1)).save(Mockito.any(PostsContent.class));
        Assertions.assertNotNull(postsVO);
    }

    @Test
    void remove() {
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Posts.class));

        postsService.remove("2112JK02");

        verify(this.postsRepository, times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void increaseViewed() {
        postsService.increaseViewed(1L);

        verify(this.postsRepository, times(1)).increaseViewed(Mockito.anyLong());
    }

}
