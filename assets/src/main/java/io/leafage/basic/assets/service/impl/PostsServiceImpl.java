/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.Category;
import io.leafage.basic.assets.domain.Post;
import io.leafage.basic.assets.domain.PostContent;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostContentRepository;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostContentVO;
import io.leafage.basic.assets.vo.PostVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;

/**
 * posts service impl.
 *
 * @author liwenqiang  2018-12-20 9:54
 **/
@Service
public class PostsServiceImpl implements PostsService {

    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;
    private final CategoryRepository categoryRepository;

    public PostsServiceImpl(PostRepository postRepository, PostContentRepository postContentRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.postContentRepository = postContentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<PostVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "modifyTime"));
        return postRepository.findByEnabledTrue(pageable).map(this::convertOuter);
    }

    @Override
    public PostVO fetch(Long id) {
        Assert.notNull(id, "id cannot be null.");
        //查询基本信息
        Post post = postRepository.getByCodeAndEnabledTrue(id);
        if (post == null) {
            return null;
        }
        return this.convertOuter(post);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PostContentVO details(Long id) {
        Assert.notNull(id, "id cannot be null.");
        Post post = postRepository.getByCodeAndEnabledTrue(id);
        if (post == null) {
            return null;
        }
        // viewed自增一，异步执行
        this.increaseViewed(post.getId());

        PostVO vo = this.convertOuter(post);
        PostContentVO postsContentVO = new PostContentVO();
        BeanUtils.copyProperties(vo, postsContentVO);
        postsContentVO.setPostsId(post.getId());
        // 获取内容详情
        PostContent postContent = postContentRepository.getByPostsIdAndEnabledTrue(post.getId());
        if (postContent != null) {
            postsContentVO.setContent(postContent.getContent());
            postsContentVO.setCatalog(postContent.getCatalog());
        }
        return postsContentVO;
    }

    @Override
    public PostVO next(Long id) {
        Assert.notNull(id, "id cannot be null.");
        Post post = postRepository.getByCodeAndEnabledTrue(id);
        Post next = postRepository.getFirstByIdGreaterThanAndEnabledTrueOrderByIdAsc(post.getId());
        return this.convertOuter(next);
    }

    @Override
    public PostVO previous(Long id) {
        Assert.notNull(id, "id cannot be null.");
        Post post = postRepository.getByCodeAndEnabledTrue(id);
        Post previous = postRepository.getFirstByIdLessThanAndEnabledTrueOrderByIdDesc(post.getId());
        return this.convertOuter(previous);
    }

    @Override
    public boolean exist(String title) {
        return postRepository.existsByTitle(title);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PostVO create(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        post.setTags(postDTO.getTags().toString());
        // 保存并立即刷盘
        post = postRepository.saveAndFlush(post);
        //保存帖子内容
        PostContent postContent = postContentRepository.getByPostsIdAndEnabledTrue(post.getId());
        if (postContent == null) {
            postContent = new PostContent();
        }
        postContent.setPostsId(post.getId());
        postContent.setContent(postDTO.getContent());
        postContentRepository.saveAndFlush(postContent);
        //转换结果
        return this.convertOuter(post);
    }

    @Override
    public PostVO modify(Long id, PostDTO postDTO) {
        Assert.notNull(id, "id cannot be null.");
        //查询基本信息
        Post post = postRepository.getByCodeAndEnabledTrue(id);
        if (post == null) {
            return null;
        }
        BeanUtils.copyProperties(postDTO, post);
        post.setTags(postDTO.getTags().toString());

        post = postRepository.save(post);
        //保存文章内容
        PostContent postContent = postContentRepository.getByPostsIdAndEnabledTrue(post.getId());
        if (postContent == null) {
            postContent = new PostContent();
        }
        postContent.setContent(postDTO.getContent());
        postContentRepository.save(postContent);
        //转换结果
        return this.convertOuter(post);
    }

    @Override
    public void remove(Long id) {
        Assert.notNull(id, "id cannot be null.");

        postRepository.deleteById(id);
    }

    private void increaseViewed(Long id) {
        Assert.notNull(id, "id must not be null");
        postRepository.increaseViewed(id);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param post 信息
     * @return 输出转换后的vo对象
     */
    private PostVO convertOuter(Post post) {
        PostVO vo = new PostVO();
        BeanUtils.copyProperties(post, vo);
        // 转换 tags
        String tags = post.getTags().substring(1, post.getTags().length() - 1)
                .replace(" ", "").replace("\"", "");
        vo.setTags(Set.of(tags.split(",")));
        // 转换分类
        Optional<Category> optional = categoryRepository.findById(post.getCategoryId());
        optional.ifPresent(category -> vo.setCategory(category.getName()));
        return vo;
    }

}
