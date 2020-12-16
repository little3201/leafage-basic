/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service;

import org.springframework.data.domain.Page;
import top.abeille.basic.assets.dto.PostsDTO;
import top.abeille.basic.assets.vo.PostsVO;
import top.abeille.common.basic.BasicService;

/**
 * 文章信息Service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface PostsService extends BasicService<PostsDTO, PostsVO> {

    Page<PostsVO> retrieves(int page, int size);
}
