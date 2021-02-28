/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.vo.ContentVO;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
import io.leafage.common.basic.BasicService;
import reactor.core.publisher.Mono;

/**
 * posts service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface PostsService extends BasicService<PostsDTO, PostsVO> {

    /**
     * 根据代码查询详细信息
     *
     * @param code 代码
     * @return 详细信息
     */
    Mono<PostsContentVO> fetchDetails(String code);

    /**
     * 根据代码查询内容
     *
     * @param code 代码
     * @return 详细信息
     */
    Mono<ContentVO> fetchContent(String code);

    /**
     * 下一条记录
     *
     * @param code 代码
     * @return 帖子信息
     */
    Mono<PostsVO> nextPosts(String code);

    /**
     * 上一条记录
     *
     * @param code 代码
     * @return 帖子信息
     */
    Mono<PostsVO> previousPosts(String code);
}
