/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.abeille.basic.assets.document.Content;
import top.abeille.basic.assets.dto.PostsDTO;
import top.abeille.basic.assets.entity.Posts;
import top.abeille.basic.assets.repository.ContentRepository;
import top.abeille.basic.assets.repository.PostsRepository;
import top.abeille.basic.assets.service.PostsService;
import top.abeille.basic.assets.vo.PostsVO;
import top.abeille.common.basic.AbstractBasicService;

/**
 * 文章信息service实现
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class PostsServiceImpl extends AbstractBasicService implements PostsService {

    private final PostsRepository postsRepository;
    private final ContentRepository contentRepository;

    public PostsServiceImpl(PostsRepository postsRepository, ContentRepository contentRepository) {
        this.postsRepository = postsRepository;
        this.contentRepository = contentRepository;
    }

    @Override
    public Page<PostsVO> retrieves(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postsRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public PostsVO fetch(String code) {
        //去mysql中查询基本信息
        Posts posts = postsRepository.findByCodeAndEnabledTrue(code);
        if (posts == null) {
            return null;
        }
        PostsVO postsVO = this.convertOuter(posts);
        Content content = contentRepository.findByPostsIdAndEnabledTrue(posts.getId());
        if (content != null) {
            postsVO.setContent(content.getContent());
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
        Content content = contentRepository.findByPostsIdAndEnabledTrue(posts.getId());
        if (content == null) {
            content = new Content();
        }
        content.setTitle(postsDTO.getTitle());
        content.setContent(postsDTO.getContent());
        contentRepository.save(content);
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

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private PostsVO convertOuter(Posts info) {
        PostsVO outer = new PostsVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }

}
