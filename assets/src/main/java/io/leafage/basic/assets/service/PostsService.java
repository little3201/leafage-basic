/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.vo.PostsVO;
import io.leafage.common.basic.BasicService;
import org.springframework.data.domain.Page;

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
    Page<PostsVO> retrieves(int page, int size);

}
