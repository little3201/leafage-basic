/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.DetailsInfo;

public interface DetailsService {

    /**
     * 新增信息
     *
     * @param detailsInfo 内容信息
     * @return 返回操作结果，否则返回empty
     */
    Mono<DetailsInfo> create(DetailsInfo detailsInfo);

    /**
     * 根据文章ID修改信息
     *
     * @param articleId   文章ID
     * @param detailsInfo 信息
     * @return 返回操作结果，否则返回empty
     */
    Mono<DetailsInfo> modify(String articleId, DetailsInfo detailsInfo);

    /**
     * 根据文章ID查询
     *
     * @param articleId 文章ID
     * @return 返回查询到的信息，否则返回empty
     */
    Mono<DetailsInfo> fetchByArticleId(String articleId);
}
