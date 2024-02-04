package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.Comment;
import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.repository.CommentRepository;
import io.leafage.basic.assets.repository.PostStatisticsRepository;
import io.leafage.basic.assets.service.CommentService;
import io.leafage.basic.assets.vo.CommentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * comment service impl.
 *
 * @author wq li 2021/09/29 15:10
 **/
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostStatisticsRepository postStatisticsRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostStatisticsRepository postStatisticsRepository) {
        this.commentRepository = commentRepository;
        this.postStatisticsRepository = postStatisticsRepository;
    }

    @Override
    public Page<CommentVO> retrieve(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public List<CommentVO> relation(Long id) {
        Assert.notNull(id, "comment id must not be null.");
        return commentRepository.findByPostIdAndReplierIsNull(id)
                .stream().map(this::convertOuter).toList();
    }

    @Override
    public List<CommentVO> replies(Long replier) {
        return commentRepository.findByReplier(replier)
                .stream().map(this::convertOuter).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentVO create(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment = commentRepository.saveAndFlush(comment);
        // 添加关联帖子的评论数
        this.postStatisticsRepository.increaseComment(comment.getPostId());
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

        Long count = commentRepository.countByReplier(comment.getId());
        vo.setCount(count);
        return vo;
    }

}
