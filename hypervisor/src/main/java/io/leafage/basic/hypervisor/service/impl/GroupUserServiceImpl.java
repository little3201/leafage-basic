package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.service.GroupUserService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GroupUserServiceImpl implements GroupUserService {

    @Override
    public Flux<UserVO> groupRelation(String code) {
        return null;
    }

    @Override
    public Flux<GroupVO> userRelation(String code) {
        return null;
    }
}
