/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.BasicService;

/**
 * 角色信息service 接口
 *
 * @author liwenqiang 2018/9/27 14:18
 **/
public interface RoleService extends BasicService<RoleDTO, RoleVO> {

    /**
     * 分页查询
     *
     * @param page  页码
     * @param size  大小
     * @param order 排序字段
     * @return 查询结果
     */
    Page<RoleVO> retrieve(int page, int size, String order);
}
