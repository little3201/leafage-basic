package io.leafage.basic.assets.service.impl;

import com.mongodb.client.result.UpdateResult;
import io.leafage.basic.assets.document.Comment;
import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.repository.CommentRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.CommentService;
import io.leafage.basic.assets.vo.CommentVO;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;

import javax.naming.NotContextException;
import java.util.NoSuchElementException;
/**
 * comment service impl
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Service
public class CommentServiceImpl extends AbstractBasicService implements CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public CommentServiceImpl(CommentRepository commentRepository, PostsRepository postsRepository,
                              ReactiveMongoTemplate reactiveMongoTemplate) {
        this.commentRepository = commentRepository;
        this.postsRepository = postsRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<CommentVO> retrieve(int page, int size) {
        return commentRepository.findByEnabledTrue(PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "modifyTime"))).flatMap(this::convertOuter);
    }

    @Override
    public Flux<CommentVO> relation(String code) {
        return postsRepository.getByCodeAndEnabledTrue(code).flatMapMany(posts ->
                commentRepository.findByPostsIdAndEnabledTrue(posts.getId()).flatMap(this::convertOuter));
    }

    @Override
    public Flux<CommentVO> replies(String replier) {
        return commentRepository.findByReplierAndEnabledTrue(replier).flatMap(this::convertOuter);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<CommentVO> create(CommentDTO commentDTO) {
        return postsRepository.getByCodeAndEnabledTrue(commentDTO.getPosts()).map(posts -> {
                    Comment comment = new Comment();
                    BeanUtils.copyProperties(commentDTO, comment);
                    comment.setCode(this.generateCode());
                    comment.setPostsId(posts.getId());
                    return comment;
                }).switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(comment -> commentRepository.insert(comment).flatMap(comm ->
                        this.incrementComment(comm.getPostsId()).map(updateResult -> comm)))
                .flatMap(this::convertOuter);
    }

    @Override
    public Mono<CommentVO> modify(String code, CommentDTO commentDTO) {
        Assert.hasText(code, "code is blank");
        return commentRepository.getByCodeAndEnabledTrue(code).doOnNext(comment ->
                        BeanUtils.copyProperties(commentDTO, comment)).switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(commentRepository::save).flatMap(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param comment 信息
     * @return 输出转换后的vo对象
     */
    private Mono<CommentVO> convertOuter(Comment comment) {
        return Mono.just(comment).map(c -> {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(c, commentVO);
            return commentVO;
        }).flatMap(commentVO -> postsRepository.findById(comment.getPostsId())
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .map(posts -> {
                    commentVO.setPosts(posts.getCode());
                    return commentVO;
                })).flatMap(commentVO -> {
            if (StringUtils.hasText(comment.getReplier())) {
                return commentRepository.countByReplierAndEnabledTrue(comment.getReplier())
                        .switchIfEmpty(Mono.just(0L)).map(count -> {
                            commentVO.setCount(count);
                            return commentVO;
                        });
            }
            return Mono.just(commentVO);
        });
    }

    /**
     * comment 原子更新（自增 1）
     *
     * @param id 主键
     * @return UpdateResult
     */
    private Mono<UpdateResult> incrementComment(ObjectId id) {
        return reactiveMongoTemplate.upsert(Query.query(Criteria.where("id").is(id)),
                new Update().inc("comment", 1), Posts.class);
    }

}
