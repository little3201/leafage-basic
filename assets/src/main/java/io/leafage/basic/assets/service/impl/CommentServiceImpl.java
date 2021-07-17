package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Comment;
import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.repository.CommentRepository;
import io.leafage.basic.assets.service.CommentService;
import io.leafage.basic.assets.vo.CommentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;

import javax.naming.NotContextException;

@Service
public class CommentServiceImpl extends AbstractBasicService implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Flux<CommentVO> retrieve(int page, int size, String order) {
        return commentRepository.findByEnabledTrue(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,
                StringUtils.hasText(order) ? order : "modifyTime"))).map(this::convertOuter);
    }

    @Override
    public Mono<CommentVO> create(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setCode(this.generateCode());
        return commentRepository.insert(comment).map(this::convertOuter);
    }

    @Override
    public Mono<CommentVO> modify(String code, CommentDTO commentDTO) {
        Assert.hasText(code, "code is blank");
        return commentRepository.getByCodeAndEnabledTrue(code).doOnNext(comment ->
                BeanUtils.copyProperties(commentDTO, comment)).switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(commentRepository::save).map(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private CommentVO convertOuter(Comment info) {
        CommentVO outer = new CommentVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
