package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.entity.Comment;
import io.leafage.basic.assets.entity.Posts;
import io.leafage.basic.assets.repository.CommentRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.CommentService;
import io.leafage.basic.assets.vo.CommentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import top.leafage.common.basic.AbstractBasicService;
import top.leafage.common.basic.ValidMessage;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * comment service impl.
 *
 * @author liwenqiang 2021/09/29 15:10
 **/
@Service
public class CommentServiceImpl extends AbstractBasicService implements CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostsRepository postsRepository) {
        this.commentRepository = commentRepository;
        this.postsRepository = postsRepository;
    }

    @Override
    public Page<CommentVO> retrieve(int page, int size) {
        return commentRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public List<CommentVO> relation(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Posts posts = postsRepository.getByCodeAndEnabledTrue(code);
        if (null == posts) {
            return Collections.emptyList();
        }
        return commentRepository.findByPostsIdAndReplierIsNullAndEnabledTrue(posts.getId())
                .stream().map(this::convertOuter).collect(Collectors.toList());
    }

    @Override
    public List<CommentVO> replies(String replier) {
        return commentRepository.findByReplierAndEnabledTrue(replier)
                .stream().map(this::convertOuter).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentVO create(CommentDTO commentDTO) {
        Posts posts = postsRepository.getByCodeAndEnabledTrue(commentDTO.getPosts());
        if (null == posts) {
            return null;
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setCode(this.generateCode());
        comment.setPostsId(posts.getId());
        comment = commentRepository.saveAndFlush(comment);
        // 添加关联帖子的评论数
        this.increaseComment(comment.getPostsId());
        return this.convertOuter(comment);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param comment 信息
     * @return 输出转换后的vo对象
     */
    private CommentVO convertOuter(Comment comment) {
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(comment, vo);

        Optional<Posts> optional = postsRepository.findById(comment.getPostsId());
        optional.ifPresent(o -> vo.setPosts(o.getCode()));

        Long count = commentRepository.countByReplierAndEnabledTrue(comment.getCode());
        vo.setCount(count);
        return vo;
    }

    /**
     * comment 原子更新（自增 1）
     *
     * @param id 主键
     */
    private void increaseComment(long id) {
        postsRepository.increaseComment(id);
    }

}
