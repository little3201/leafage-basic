/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import top.leafage.common.reactive.ReactiveBasicService;
import top.leafage.common.reactive.ReactiveTreeNodeAware;

/**
 * 权限信息Service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface AuthorityService extends ReactiveBasicService<AuthorityDTO, AuthorityVO>, ReactiveTreeNodeAware<Authority> {

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

    /**
     * 是否已存在
     *
     * @param name 名称
     * @return true-是，false-否
     */
    Mono<Boolean> exists(String name);
}
