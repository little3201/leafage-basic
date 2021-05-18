/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.domain.TreeNode;
import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.BasicService;

import java.util.List;

/**
 * 权限信息Service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface AuthorityService extends BasicService<AuthorityDTO, AuthorityVO> {

    /**
     * 分页查询
     *
     * @param page  页码
     * @param size  大小
     * @param order 排序字段
     * @return 查询结果
     */
    Page<AuthorityVO> retrieve(int page, int size, String order);

    List<TreeNode> tree();
}
