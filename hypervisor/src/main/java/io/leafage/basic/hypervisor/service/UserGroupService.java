package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.document.UserGroup;
import io.leafage.basic.hypervisor.dto.UserGroupDTO;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import reactor.core.publisher.Flux;

public interface UserGroupService {

    /**
     * 查询关联用户
     *
     * @param code 代码
     * @return 数据集
     */
    Flux<UserVO> groupRelation(String code);

    /**
     * 查询关联分组
     *
     * @param code 代码
     * @return 数据集
     */
    Flux<GroupVO> userRelation(String code);

    /**
     * 保存用户-分组关系
     *
     * @param userGroupDTO 用户-分组信息
     * @return 结果集
     */
    Flux<UserGroup> create(UserGroupDTO userGroupDTO);
}
