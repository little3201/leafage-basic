/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.ContentInfo;

public interface ContentInfoService {

    /**
     * 新增信息
     *
     * @param contentInfo 内容信息
     * @return 返回操作结果，否则返回empty
     */
    Mono<ContentInfo> create(ContentInfo contentInfo);

    /**
     * 根据业务id修改信息
     *
     * @param businessId  业务id
     * @param contentInfo 信息
     * @return 返回操作结果，否则返回empty
     */
    Mono<ContentInfo> modify(String businessId, ContentInfo contentInfo);

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    Mono<ContentInfo> fetchByBusinessIdId(String businessId);
}
