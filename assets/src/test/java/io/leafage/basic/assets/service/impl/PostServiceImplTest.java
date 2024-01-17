/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;


import io.leafage.basic.assets.domain.Post;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.domain.Category;
import io.leafage.basic.assets.domain.PostContent;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostContentRepository;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.vo.PostVO;
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
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostContentRepository postContentRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private PostsServiceImpl postsService;

    @Test
    void retrieve_page() {
        Post post = new Post();
        post.setTitle("test");
        post.setTags("java,spring");
        Page<Post> postsPage = new PageImpl<>(List.of(post));
        given(this.postRepository.findByEnabledTrue(PageRequest.of(0, 2, Sort.by("id")))).willReturn(postsPage);

        Page<PostVO> voPage = postsService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        Post post = new Post();
        post.setId(12L);
        post.setTags("测试,test");
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(post);

        PostVO postVO = postsService.fetch("2112JK02");

        Assertions.assertNotNull(postVO);
    }

    @Test
    void fetch_posts_null() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(null);

        PostVO postVO = postsService.fetch("2112JK02");

        Assertions.assertNull(postVO);
    }

    @Test
    void next() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Post.class));

        Post post = new Post();
        post.setId(12L);
        post.setTags("测试,test");
        post.setCategoryId(2L);
        given(this.postRepository.getFirstByIdGreaterThanAndEnabledTrueOrderByIdAsc(Mockito.anyLong())).willReturn(post);

        PostVO postVO = postsService.next("2112JK02");

        Assertions.assertNotNull(postVO);
    }


    @Test
    void previous() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Post.class));

        Post post = new Post();
        post.setId(12L);
        post.setTags("测试,test");
        post.setCategoryId(2L);
        given(this.postRepository.getFirstByIdLessThanAndEnabledTrueOrderByIdDesc(Mockito.anyLong())).willReturn(post);

        PostVO postVO = postsService.previous("2112JK02");

        Assertions.assertNotNull(postVO);
    }

    @Test
    void details() {
        Post post = new Post();
        post.setId(12L);
        post.setTags("测试,test");
        post.setCategoryId(2L);
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(post);

        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Category.class)));

        PostContent postContent = new PostContent();
        postContent.setContent("测试内容");
        postContent.setPostsId(post.getId());
        postContent.setCatalog("目录");
        given(this.postContentRepository.getByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(postContent);

        PostVO postVO = postsService.details("2112JK02");

        Assertions.assertNotNull(postVO);
    }

    @Test
    void details_posts_null() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(null);

        PostVO postVO = postsService.details("2112JK02");

        Assertions.assertNull(postVO);
    }

    @Test
    void content_posts_null() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(null);

        ContentVO contentVO = postsService.content("2112JK02");

        Assertions.assertNull(contentVO);
    }

    @Test
    void content() {
        Post post = new Post();
        post.setId(12L);
        post.setTags("测试,test");
        post.setCategoryId(2L);
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(post);

        PostContent postContent = new PostContent();
        postContent.setContent("测试内容");
        postContent.setPostsId(post.getId());
        postContent.setCatalog("目录");
        given(this.postContentRepository.getByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(postContent);

        ContentVO contentVO = postsService.content("2112JK02");

        Assertions.assertNotNull(contentVO);
    }

    @Test
    void exist() {
        given(this.postRepository.existsByTitle(Mockito.anyString())).willReturn(true);

        boolean exist = postsService.exist("spring");

        Assertions.assertTrue(exist);
    }

    @Test
    void create_content_null() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Category.class));

        Post post = new Post();
        post.setId(12L);
        post.setTags("测试,test");
        given(this.postRepository.saveAndFlush(Mockito.any(Post.class))).willReturn(post);

        PostContent postContent = new PostContent();
        postContent.setContent("测试内容");
        postContent.setPostsId(post.getId());
        postContent.setCatalog("目录");
        given(this.postContentRepository.getByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(null);

        given(this.postContentRepository.saveAndFlush(Mockito.any(PostContent.class))).willReturn(postContent);

        PostDTO postDTO = new PostDTO();
        postDTO.setCategory("2112JOP2");
        postDTO.setTags(Set.of(post.getTags().split(",")));
        PostVO postVO = postsService.create(postDTO);

        post.setId(postContent.getPostsId());
        verify(this.postRepository, times(1)).saveAndFlush(Mockito.any(Post.class));
        verify(this.postContentRepository, times(1)).saveAndFlush(Mockito.any(PostContent.class));
        Assertions.assertNotNull(postVO);
    }

    @Test
    void create() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Category.class));

        Post post = new Post();
        post.setId(12L);
        post.setTags("测试,test");
        given(this.postRepository.saveAndFlush(Mockito.any(Post.class))).willReturn(post);

        PostContent postContent = new PostContent();
        postContent.setContent("测试内容");
        postContent.setPostsId(post.getId());
        postContent.setCatalog("目录");
        given(this.postContentRepository.getByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(postContent);

        given(this.postContentRepository.saveAndFlush(Mockito.any(PostContent.class))).willReturn(postContent);

        PostDTO postDTO = new PostDTO();
        postDTO.setCategory("2112JOP2");
        postDTO.setTags(Set.of(post.getTags().split(",")));
        PostVO postVO = postsService.create(postDTO);

        post.setId(postContent.getPostsId());
        verify(this.postRepository, times(1)).saveAndFlush(Mockito.any(Post.class));
        verify(this.postContentRepository, times(1)).saveAndFlush(Mockito.any(PostContent.class));
        Assertions.assertNotNull(postVO);
    }

    @Test
    void modify_category_null() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Post.class));

        Post post = new Post();
        post.setId(12L);
        post.setTags("测试,test");
        given(this.postRepository.save(Mockito.any(Post.class))).willReturn(post);

        given(this.postContentRepository.getByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(PostContent.class));

        given(this.postContentRepository.save(Mockito.any(PostContent.class))).willReturn(Mockito.mock(PostContent.class));

        PostDTO postDTO = new PostDTO();
        postDTO.setCategory("2112JOP2");
        postDTO.setTags(Set.of("test1", "test2"));
        PostVO postVO = postsService.modify("2112JK02", postDTO);

        verify(this.postRepository, times(1)).save(Mockito.any(Post.class));
        verify(this.postContentRepository, times(1)).save(Mockito.any(PostContent.class));
        Assertions.assertNotNull(postVO);
    }

    @Test
    void modify_posts_null() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(null);

        PostVO postVO = postsService.modify("2112JK02", Mockito.mock(PostDTO.class));

        Assertions.assertNull(postVO);
    }


    @Test
    void modify() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Post.class));

        Category category = new Category();
        category.setId(1L);
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(category);

        Post post = new Post();
        post.setId(12L);
        post.setTags("测试,test");
        given(this.postRepository.save(Mockito.any(Post.class))).willReturn(post);

        given(this.postContentRepository.getByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(PostContent.class));

        given(this.postContentRepository.save(Mockito.any(PostContent.class))).willReturn(Mockito.mock(PostContent.class));

        PostDTO postDTO = new PostDTO();
        postDTO.setCategory("2112JOP2");
        postDTO.setTags(Set.of("test1", "test2"));
        PostVO postVO = postsService.modify("2112JK02", postDTO);

        verify(this.postRepository, times(1)).save(Mockito.any(Post.class));
        verify(this.postContentRepository, times(1)).save(Mockito.any(PostContent.class));
        Assertions.assertNotNull(postVO);
    }

    @Test
    void modify_error() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Post.class));

        Post post = new Post();
        post.setId(12L);
        post.setTags("测试,test");
        given(this.postRepository.save(Mockito.any(Post.class))).willReturn(post);

        given(this.postContentRepository.getByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(null);

        given(this.postContentRepository.save(Mockito.any(PostContent.class))).willReturn(Mockito.mock(PostContent.class));

        PostVO postVO = postsService.modify("2112JK02", Mockito.mock(PostDTO.class));

        verify(this.postRepository, times(1)).save(Mockito.any(Post.class));
        verify(this.postContentRepository, times(1)).save(Mockito.any(PostContent.class));
        Assertions.assertNotNull(postVO);
    }

    @Test
    void remove() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Post.class));

        postsService.remove("2112JK02");

        verify(this.postRepository, times(1)).deleteById(Mockito.anyLong());
    }

}
