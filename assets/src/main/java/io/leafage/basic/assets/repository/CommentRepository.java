package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * comment repository
 *
 * @author liwenqiang  2021-09-29 14:19
 **/
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效评论
     */
    Page<Comment> findByEnabledTrue(Pageable pageable);

    /**
     * 根据帖子ID查询
     *
     * @param postsId 帖子ID
     * @return 关联的数据
     */
    List<Comment> findByPostsIdAndEnabledTrue(long postsId);

    /**
     * 根据code查询信息
     *
     * @param code 代码
     * @return 评论信息
     */
    Comment getByCodeAndEnabledTrue(String code);
}
