/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
import org.springframework.data.domain.Page;
import top.leafage.common.TreeNode;
import top.leafage.common.servlet.ServletBasicService;

import java.util.List;

/**
 * privilege service.
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface PrivilegeService extends ServletBasicService<PrivilegeDTO, PrivilegeVO> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 查询结果
     */
    Page<PrivilegeVO> retrieve(int page, int size, String sort);

    /**
     * 获取树结构数据
     *
     * @return 树结构数据集
     */
    List<TreeNode> tree();

}
