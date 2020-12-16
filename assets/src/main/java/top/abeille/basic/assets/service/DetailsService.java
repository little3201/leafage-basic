/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.Details;

/**
 * 内容service接口
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
public interface DetailsService {

    /**
     * 新增信息
     *
     * @param details 内容信息
     * @return 返回操作结果，否则返回empty
     */
    Mono<Details> create(Details details);

    /**
     * 根据文章ID修改信息
     *
     * @param articleId 文章ID
     * @param details   信息
     * @return 返回操作结果，否则返回empty
     */
    Mono<Details> modify(String articleId, Details details);

    /**
     * 根据文章ID查询
     *
     * @param articleId 文章ID
     * @return 返回查询到的信息，否则返回empty
     */
    Mono<Details> fetchByPostsId(String articleId);
}
