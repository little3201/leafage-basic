/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import reactor.core.publisher.Flux;
import top.leafage.common.basic.TreeNode;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * 权限信息Service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface AuthorityService extends ReactiveBasicService<AuthorityDTO, AuthorityVO, String> {

    /**
     * 查询构造树结构的数据
     *
     * @return 数据集
     */
    Flux<TreeNode> tree();

    /**
     * 查询用户权限
     *
     * @param username 用户名
     * @return 权限树
     */
    Flux<TreeNode> authorities(String username);

}
