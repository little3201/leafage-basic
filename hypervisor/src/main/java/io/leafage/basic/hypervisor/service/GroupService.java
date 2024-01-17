/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.springframework.data.domain.Page;
import top.leafage.common.TreeNode;
import top.leafage.common.servlet.ServletBasicService;

import java.util.List;

/**
 * group service.
 *
 * @author liwenqiang 2018/12/17 19:24
 **/
public interface GroupService extends ServletBasicService<GroupDTO, GroupVO> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 查询结果
     */
    Page<GroupVO> retrieve(int page, int size, String sort);

    /**
     * 获取树结构数据
     *
     * @return 树结构数据集
     */
    List<TreeNode> tree();
}
