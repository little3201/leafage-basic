package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Role;
import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.document.UserRole;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.BDDMockito.given;

/**
 * user-role接口测试
 *
 * @author liwenqiang 2021/6/14 11:10
 **/
@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    @Test
    void users() {
        Role role = new Role();
        ObjectId id = new ObjectId();
        role.setId(id);
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(role));

        UserRole userRole = new UserRole();
        userRole.setRoleId(id);
        userRole.setUserId(new ObjectId());
        given(this.userRoleRepository.findByRoleId(Mockito.any(ObjectId.class))).willReturn(Flux.just(userRole));

        given(this.userRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(User.class)));
        StepVerifier.create(userRoleService.users("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void roles() {
        User user = new User();
        ObjectId id = new ObjectId();
        user.setId(id);
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(user));

        UserRole userRole = new UserRole();
        userRole.setUserId(id);
        userRole.setRoleId(new ObjectId());
        given(this.userRoleRepository.findByUserId(Mockito.any(ObjectId.class))).willReturn(Flux.just(userRole));

        given(this.roleRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Role.class)));

        StepVerifier.create(userRoleService.roles("little3201")).expectNextCount(1).verifyComplete();
    }

    @Test
    void relation() {
        User user = new User();
        ObjectId id = new ObjectId();
        user.setId(id);
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(user));

        Role role = new Role();
        role.setId(new ObjectId());
        given(this.roleRepository.findByCodeInAndEnabledTrue(Mockito.anyCollection())).willReturn(Flux.just(role));

        given(this.userRoleRepository.saveAll(Mockito.anyCollection())).willReturn(Flux.just(Mockito.mock(UserRole.class)));

        StepVerifier.create(userRoleService.relation("little3201", Collections.singleton("21612OL34")))
                .expectNextCount(1).verifyComplete();
    }
}