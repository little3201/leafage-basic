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
import io.leafage.basic.assets.vo.ContentVO;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.leafage.common.basic.AbstractBasicService;
import top.leafage.common.basic.ValidMessage;
import java.util.Optional;
import java.util.Set;

/**
 * posts service impl.
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
    public Page<PostsVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "modifyTime"));
        return postsRepository.findByEnabledTrue(pageable).map(this::convertOuter);
    }

    @Override
    public PostsVO fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        //查询基本信息
        Posts posts = postsRepository.getByCodeAndEnabledTrue(code);
        if (posts == null) {
            return null;
        }
        return this.convertOuter(posts);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PostsContentVO details(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Posts posts = postsRepository.getByCodeAndEnabledTrue(code);
        if (posts == null) {
            return null;
        }
        // viewed自增一，异步执行
        this.increaseViewed(posts.getId());

        PostsVO vo = this.convertOuter(posts);
        PostsContentVO postsContentVO = new PostsContentVO();
        BeanUtils.copyProperties(vo, postsContentVO);
        postsContentVO.setPostsId(posts.getId());
        // 获取内容详情
        PostsContent postsContent = postsContentRepository.getByPostsIdAndEnabledTrue(posts.getId());
        if (postsContent != null) {
            postsContentVO.setContent(postsContent.getContent());
            postsContentVO.setCatalog(postsContent.getCatalog());
        }
        return postsContentVO;
    }

    @Override
    public ContentVO content(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Posts posts = postsRepository.getByCodeAndEnabledTrue(code);
        if (posts == null) {
            return null;
        }
        ContentVO contentVO = new ContentVO();
        // 获取内容详情
        PostsContent postsContent = postsContentRepository.getByPostsIdAndEnabledTrue(posts.getId());
        if (postsContent != null) {
            contentVO.setContent(postsContent.getContent());
            contentVO.setCatalog(postsContent.getCatalog());
        }
        return contentVO;
    }

    @Override
    public PostsVO next(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Posts posts = postsRepository.getByCodeAndEnabledTrue(code);
        Posts next = postsRepository.getFirstByIdGreaterThanAndEnabledTrueOrderByIdAsc(posts.getId());
        return this.convertOuter(next);
    }

    @Override
    public PostsVO previous(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Posts posts = postsRepository.getByCodeAndEnabledTrue(code);
        Posts previous = postsRepository.getFirstByIdLessThanAndEnabledTrueOrderByIdDesc(posts.getId());
        return this.convertOuter(previous);
    }

    @Override
    public boolean exist(String title) {
        return postsRepository.existsByTitle(title);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PostsVO create(PostsDTO postsDTO) {
        Posts posts = new Posts();
        BeanUtils.copyProperties(postsDTO, posts);
        posts.setCode(this.generateCode());
        posts.setTags(postsDTO.getTags().toString());
        if (StringUtils.hasText(postsDTO.getCategory())) {
            Category category = categoryRepository.getByCodeAndEnabledTrue(postsDTO.getCategory());
            if (category != null) {
                posts.setCategoryId(category.getId());
            }
        }
        // 保存并立即刷盘
        posts = postsRepository.saveAndFlush(posts);
        //保存帖子内容
        PostsContent postsContent = postsContentRepository.getByPostsIdAndEnabledTrue(posts.getId());
        if (postsContent == null) {
            postsContent = new PostsContent();
        }
        postsContent.setPostsId(posts.getId());
        postsContent.setContent(postsDTO.getContent());
        postsContentRepository.saveAndFlush(postsContent);
        //转换结果
        return this.convertOuter(posts);
    }

    @Override
    public PostsVO modify(String code, PostsDTO postsDTO) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        //查询基本信息
        Posts posts = postsRepository.getByCodeAndEnabledTrue(code);
        if (posts == null) {
            return null;
        }
        BeanUtils.copyProperties(postsDTO, posts);
        posts.setTags(postsDTO.getTags().toString());
        if (StringUtils.hasText(postsDTO.getCategory())) {
            Category category = categoryRepository.getByCodeAndEnabledTrue(postsDTO.getCategory());
            if (category != null) {
                posts.setCategoryId(category.getId());
            }
        }
        posts = postsRepository.save(posts);
        //保存文章内容
        PostsContent postsContent = postsContentRepository.getByPostsIdAndEnabledTrue(posts.getId());
        if (postsContent == null) {
            postsContent = new PostsContent();
        }
        postsContent.setContent(postsDTO.getContent());
        postsContentRepository.save(postsContent);
        //转换结果
        return this.convertOuter(posts);
    }

    @Override
    public void remove(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Posts posts = postsRepository.getByCodeAndEnabledTrue(code);
        if (posts != null) {
            postsRepository.deleteById(posts.getId());
        }
    }

    private void increaseViewed(Long id) {
        Assert.notNull(id, "id must not be null");
        postsRepository.increaseViewed(id);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param posts 信息
     * @return 输出转换后的vo对象
     */
    private PostsVO convertOuter(Posts posts) {
        PostsVO vo = new PostsVO();
        BeanUtils.copyProperties(posts, vo);
        // 转换 tags
        String tags = posts.getTags().substring(1, posts.getTags().length() - 1)
                .replace(" ", "").replace("\"", "");
        vo.setTags(Set.of(tags.split(",")));
        // 转换分类
        Optional<Category> optional = categoryRepository.findById(posts.getCategoryId());
        optional.ifPresent(category -> vo.setCategory(category.getName()));
        return vo;
    }

}
