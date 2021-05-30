package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.service.UserRoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Override
    public Flux<UserVO> roleRelation(String code) {
        return null;
    }

    @Override
    public Flux<RoleVO> userRelation(String username) {
        return null;
    }
}
