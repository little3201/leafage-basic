package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, ObjectId> {


    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效评论
     */
    Flux<Comment> findByEnabledTrue(Pageable pageable);

    /**
     * 根据code查询信息
     *
     * @param code 代码
     * @return 评论信息
     */
    Mono<Comment> getByCodeAndEnabledTrue(String code);
}
