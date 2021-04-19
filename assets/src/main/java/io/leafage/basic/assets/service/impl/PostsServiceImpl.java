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
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
import io.leafage.common.basic.AbstractBasicService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 帖子信息service实现
 *
 * @author liwenqiang  2018-12-20 9:54
 **/
@Service
public class PostsServiceImpl extends AbstractBasicService implements PostsService {

    private final PostsRepository postsRepository;
    private final PostsContentRepository postsContentRepository;
    private final CategoryRepository categoryRepository;

    public PostsServiceImpl(PostsRepository postsRepository, PostsContentRepository postsContentRepository, CategoryRepository categoryRepository) {
        this.postsRepository = postsRepository;
        this.postsContentRepository = postsContentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<PostsVO> retrieve() {
        return postsRepository.findAll().stream().map(this::convertOuter).collect(Collectors.toList());
    }

    @Override
    public Page<PostsVO> retrieve(int page, int size, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(order) ? order : "modify_time"));
        return postsRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public PostsVO fetch(String code) {
        //查询基本信息
        Posts posts = postsRepository.findByCodeAndEnabledTrue(code);
        if (posts == null) {
            return null;
        }
        return this.convertOuter(posts);
    }

    @Override
    public PostsContentVO fetchDetails(String code) {
        //查询基本信息
        Posts posts = postsRepository.findByCodeAndEnabledTrue(code);
        // viewed自增一，异步执行
        this.flushViewed(posts.getId());
        PostsContentVO postsContentVO = new PostsContentVO();
        BeanUtils.copyProperties(posts, postsContentVO);
        postsContentVO.setPostsId(posts.getId());
        postsContentVO.setViewed(posts.getViewed() + 1);
        // 转换分类
        Optional<Category> optional = categoryRepository.findById(posts.getCategoryId());
        optional.ifPresent(category -> postsContentVO.setCategory(category.getName()));
        // 获取内容详情
        PostsContent postsContent = postsContentRepository.findByPostsIdAndEnabledTrue(posts.getId());
        if (postsContent != null) {
            postsContentVO.setContent(postsContent.getContent());
        }
        return postsContentVO;
    }

    @Override
    @Transactional
    public PostsVO create(PostsDTO postsDTO) {
        Posts posts = new Posts();
        BeanUtils.copyProperties(postsDTO, posts);
        posts.setCode(this.generateCode());
        posts = postsRepository.save(posts);
        if (posts.getId() == null) {
            return null;
        }
        //保存帖子内容
        PostsContent postsContent = postsContentRepository.findByPostsIdAndEnabledTrue(posts.getId());
        if (postsContent == null) {
            postsContent = new PostsContent();
        }
        postsContent.setPostsId(posts.getId());
        postsContent.setContent(postsDTO.getContent());
        postsContentRepository.save(postsContent);
        //转换结果
        PostsVO postsVO = this.convertOuter(posts);
        postsVO.setContent(postsDTO.getContent());
        return postsVO;
    }

    @Override
    public PostsVO modify(String code, PostsDTO postsDTO) {
        //查询基本信息
        Posts posts = postsRepository.findByCodeAndEnabledTrue(code);
        if (posts == null) {
            return null;
        }
        BeanUtils.copyProperties(postsDTO, posts);
        postsRepository.saveAndFlush(posts);
        //保存文章内容
        PostsContent postsContent = postsContentRepository.findByPostsIdAndEnabledTrue(posts.getId());
        if (postsContent == null) {
            postsContent = new PostsContent();
        }
        postsContent.setContent(postsDTO.getContent());
        postsContentRepository.saveAndFlush(postsContent);
        //转换结果
        PostsVO postsVO = this.convertOuter(posts);
        postsVO.setContent(postsDTO.getContent());
        return postsVO;
    }

    @Override
    public void remove(String code) {
        Posts posts = postsRepository.findByCodeAndEnabledTrue(code);
        if (posts != null) {
            postsRepository.deleteById(posts.getId());
        }
    }

    @Async
    public void flushViewed(long id) {
        postsRepository.flushViewed(id);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private PostsVO convertOuter(Posts info) {
        PostsVO outer = new PostsVO();
        BeanUtils.copyProperties(info, outer);
        // 转换分类
        Optional<Category> optional = categoryRepository.findById(info.getCategoryId());
        optional.ifPresent(category -> outer.setCategory(category.getName()));
        return outer;
    }

}
