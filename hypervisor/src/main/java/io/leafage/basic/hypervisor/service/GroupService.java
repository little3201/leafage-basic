/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.vo.GroupVO;
import reactor.core.publisher.Flux;
import top.leafage.common.basic.TreeNode;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * 分组信息Service
 *
 * @author liwenqiang 2018/12/17 19:24
 **/
public interface GroupService extends ReactiveBasicService<GroupDTO, GroupVO, String> {

    /**
     * 查询构造树结构的数据
     *
     * @return 数据集
     */
    Flux<TreeNode> tree();

}
