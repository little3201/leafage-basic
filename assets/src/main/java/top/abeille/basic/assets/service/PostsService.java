/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.PostsDTO;
import top.abeille.basic.assets.vo.CountVO;
import top.abeille.basic.assets.vo.PostsContentVO;
import top.abeille.basic.assets.vo.PostsVO;
import top.abeille.common.basic.BasicService;

import java.util.Set;

/**
 * 帖子信息Service
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
    Mono<PostsContentVO> fetchDetailsByCode(String code);

    /**
     * 统计关联信息
     *
     * @param ids ID集合
     * @return 统计信息
     */
    Flux<CountVO> countRelations(Set<String> ids);
}
