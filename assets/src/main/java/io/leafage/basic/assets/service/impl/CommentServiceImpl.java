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
import org.springframework.util.Assert;
import top.leafage.common.basic.AbstractBasicService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * comment service 实现
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
    public List<CommentVO> posts(String code) {
        Assert.hasText(code, "code is blank.");
        Posts posts = postsRepository.getByCodeAndEnabledTrue(code);
        if (posts == null) {
            return Collections.emptyList();
        }
        return commentRepository.findByPostsIdAndEnabledTrue(posts.getId())
                .stream().map(this::convertOuter).collect(Collectors.toList());
    }

    @Override
    public CommentVO create(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setCode(this.generateCode());
        comment = commentRepository.saveAndFlush(comment);
        this.incrementComment(comment.getPostsId());
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
        return vo;
    }

    /**
     * comment 原子更新（自增 1）
     *
     * @param id 主键
     */
    private void incrementComment(long id) {
        Optional<Posts> optional = postsRepository.findById(id);
        optional.ifPresent(posts -> postsRepository.flushComment(posts.getCode()));
    }
}
