/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.BasicService;

/**
 * 帖子信息Service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface PostsService extends BasicService<PostsDTO, PostsVO> {

    /**
     * 分页查询
     *
     * @param page 查询起点
     * @param size 大小
     * @return 查询结果
     */
    Page<PostsVO> retrieve(int page, int size, String order);

    /**
     * 根据code查询文章详情
     *
     * @param code 代码
     * @return 查询结果
     */
    PostsContentVO fetchDetails(String code);
}
