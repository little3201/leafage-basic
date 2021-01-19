/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.abeille.basic.assets.dto.PostsDTO;
import top.abeille.basic.assets.entity.Category;
import top.abeille.basic.assets.entity.Posts;
import top.abeille.basic.assets.entity.PostsContent;
import top.abeille.basic.assets.repository.CategoryRepository;
import top.abeille.basic.assets.repository.PostsContentRepository;
import top.abeille.basic.assets.repository.PostsRepository;
import top.abeille.basic.assets.service.PostsService;
import top.abeille.basic.assets.vo.PostsVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.Optional;

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
    public Page<PostsVO> retrieves(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postsRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public PostsVO fetch(String code) {
        //查询基本信息
        Posts posts = postsRepository.findByCodeAndEnabledTrue(code);
        if (posts == null) {
            return null;
        }
        this.flushViewed(posts.getId());
        posts.setViewed(posts.getViewed() + 1);
        PostsVO postsVO = this.convertOuter(posts);
        // 获取内容详情
        PostsContent postsContent = postsContentRepository.findByPostsIdAndEnabledTrue(posts.getId());
        if (postsContent != null) {
            postsVO.setContent(postsContent.getContent());
        }
        return postsVO;
    }

    @Override
    @Transactional
    public PostsVO create(PostsDTO postsDTO) {
        Posts posts = new Posts();
        BeanUtils.copyProperties(postsDTO, posts);
        posts.setCode(this.generateCode());
        postsRepository.save(posts);
        if (posts.getId() == null) {
            return null;
        }
        //保存文章内容
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
