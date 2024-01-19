package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * comment repository.
 *
 * @author liwenqiang  2021-09-29 14:19
 **/
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 根据postsId查询
     *
     * @param postsId 帖子ID
     * @return 关联的数据
     */
    List<Comment> findByPostsIdAndReplierIsNull(Long postsId);

    /**
     * 根据replier查询
     *
     * @param replier 回复信息
     * @return 关联的数据
     */
    List<Comment> findByReplier(Long replier);

    /**
     * 查询回复记录数
     *
     * @param replier 回复id
     * @return 记录数
     */
    Long countByReplier(Long replier);

}
