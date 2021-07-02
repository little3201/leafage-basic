package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.document.Role;
import io.leafage.basic.hypervisor.document.RoleAuthority;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
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
 * role-authority接口测试
 *
 * @author liwenqiang 2021/6/14 11:10
 **/
@ExtendWith(MockitoExtension.class)
class RoleAuthorityServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleAuthorityRepository roleAuthorityRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private RoleAuthorityServiceImpl roleAuthorityService;

    @Test
    void authorities() {
        Role role = new Role();
        ObjectId id = new ObjectId();
        role.setId(id);
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(role));

        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setAuthorityId(id);
        roleAuthority.setRoleId(new ObjectId());
        given(this.roleAuthorityRepository.findByRoleId(Mockito.any(ObjectId.class))).willReturn(Flux.just(roleAuthority));

        given(this.authorityRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Authority.class)));

        StepVerifier.create(roleAuthorityService.authorities("little3201")).expectNextCount(1).verifyComplete();
    }

    @Test
    void roles() {
        Authority authority = new Authority();
        ObjectId id = new ObjectId();
        authority.setId(id);
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(authority));

        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setAuthorityId(id);
        roleAuthority.setRoleId(new ObjectId());
        given(this.roleAuthorityRepository.findByAuthorityId(Mockito.any(ObjectId.class))).willReturn(Flux.just(roleAuthority));

        given(this.roleRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Role.class)));

        StepVerifier.create(roleAuthorityService.roles("little3201")).expectNextCount(1).verifyComplete();
    }

    @Test
    void relation() {
        Role role = new Role();
        ObjectId id = new ObjectId();
        role.setId(id);
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(role));

        Authority authority = new Authority();
        authority.setId(new ObjectId());
        given(this.authorityRepository.findByCodeInAndEnabledTrue(Mockito.anyCollection())).willReturn(Flux.just(authority));

        given(this.roleAuthorityRepository.saveAll(Mockito.anyCollection())).willReturn(Flux.just(Mockito.mock(RoleAuthority.class)));

        StepVerifier.create(roleAuthorityService.relation("little3201", Collections.singleton("21612OL34")))
                .expectNextCount(1).verifyComplete();
    }
}