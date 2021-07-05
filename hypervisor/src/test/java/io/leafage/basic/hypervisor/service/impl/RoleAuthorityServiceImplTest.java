package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Authority;
import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.entity.RoleAuthority;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.given;

/**
 * role authority service测试
 *
 * @author liwenqiang 2021/7/5 17:36
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
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Role.class));

        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setRoleId(1L);
        roleAuthority.setAuthorityId(1L);
        given(this.roleAuthorityRepository.findByRoleId(Mockito.anyLong())).willReturn(Collections.singletonList(roleAuthority));

        given(this.authorityRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Authority.class)));

        List<AuthorityVO> authorities = roleAuthorityService.authorities("test");
        Assertions.assertNotNull(authorities);
    }

    @Test
    void roles() {
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Authority.class));

        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setRoleId(1L);
        roleAuthority.setAuthorityId(1L);
        given(this.roleAuthorityRepository.findByAuthorityId(Mockito.anyLong())).willReturn(Collections.singletonList(roleAuthority));

        given(this.roleRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Role.class)));

        List<RoleVO> roles = roleAuthorityService.roles("test");
        Assertions.assertNotNull(roles);
    }

    @Test
    void relation() {
        Role role = new Role();
        role.setId(1L);
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(role);

        Authority authority = new Authority();
        authority.setId(2L);
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(authority);

        given(this.roleAuthorityRepository.saveAll(Mockito.anyCollection())).willReturn(Mockito.anyList());

        List<RoleAuthority> relation = roleAuthorityService.relation("test", Collections.singleton("test"));
        Assertions.assertNotNull(relation);
    }
}