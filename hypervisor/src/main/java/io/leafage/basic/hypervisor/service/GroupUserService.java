package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import reactor.core.publisher.Flux;

public interface GroupUserService {

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
}
