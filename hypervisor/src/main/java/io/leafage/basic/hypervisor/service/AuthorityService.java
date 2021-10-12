/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import org.springframework.data.domain.Page;
import top.leafage.common.basic.TreeNode;
import top.leafage.common.servlet.ServletBasicService;
import java.util.List;

/**
 * 权限信息Service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface AuthorityService extends ServletBasicService<AuthorityDTO, AuthorityVO, String> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 查询结果
     */
    Page<AuthorityVO> retrieve(int page, int size, String sort);

    /**
     * 获取树结构数据
     *
     * @return 树结构数据集
     */
    List<TreeNode> tree();

    /**
     * 获取用户关联的权限
     *
     * @param username 用户账号
     * @param type     类型
     * @return 树结构数据集
     */
    List<TreeNode> authorities(String username, Character type);
}
