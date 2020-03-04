/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.DetailsInfo;

public interface DetailsInfoService {

    /**
     * 新增信息
     *
     * @param detailsInfo 内容信息
     * @return 返回操作结果，否则返回empty
     */
    Mono<DetailsInfo> create(DetailsInfo detailsInfo);

    /**
     * 根据业务id修改信息
     *
     * @param businessId  业务id
     * @param detailsInfo 信息
     * @return 返回操作结果，否则返回empty
     */
    Mono<DetailsInfo> modify(String businessId, DetailsInfo detailsInfo);

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    Mono<DetailsInfo> fetchByBusinessId(String businessId);
}
