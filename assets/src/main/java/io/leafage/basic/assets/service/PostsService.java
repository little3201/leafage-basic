/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.vo.ContentVO;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

/**
 * posts service.
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface PostsService extends ServletBasicService<PostsDTO, PostsVO, String> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 查询结果
     */
    Page<PostsVO> retrieve(int page, int size, String sort);

    /**
     * 根据code查询文章详情
     *
     * @param code 代码
     * @return 查询结果
     */
    PostsContentVO details(String code);

    /**
     * 根据代码查询内容
     *
     * @param code 代码
     * @return 详细信息
     */
    ContentVO content(String code);

    /**
     * 下一篇
     *
     * @param code 代码
     * @return 信息
     */
    PostsVO next(String code);

    /**
     * 上一篇
     *
     * @param code 代码
     * @return 信息
     */
    PostsVO previous(String code);
}
