package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Privilege;
import io.leafage.basic.hypervisor.domain.Role;
import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.repository.PrivilegeRepository;
import io.leafage.basic.hypervisor.repository.RolePrivilegesRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * role privilege service test
 *
 * @author liwenqiang 2021/7/5 17:36
 **/
@ExtendWith(MockitoExtension.class)
class RolePrivilegeServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RolePrivilegesRepository rolePrivilegesRepository;

    @Mock
    private PrivilegeRepository privilegeRepository;

    @InjectMocks
    private RolePrivilegesServiceImpl roleAuthorityService;

    @Test
    void authorities() {
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Role.class));

        RolePrivileges rolePrivileges = new RolePrivileges();
        rolePrivileges.setRoleId(1L);
        rolePrivileges.setAuthorityId(1L);
        given(this.rolePrivilegesRepository.findByRoleId(Mockito.anyLong())).willReturn(Collections.singletonList(rolePrivileges));

        given(this.privilegeRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Privilege.class)));

        List<PrivilegeVO> authorities = roleAuthorityService.privileges("test");
        Assertions.assertNotNull(authorities);
    }

    @Test
    void roles() {
        given(this.privilegeRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Privilege.class));

        RolePrivileges rolePrivileges = new RolePrivileges();
        rolePrivileges.setRoleId(1L);
        rolePrivileges.setAuthorityId(1L);
        given(this.rolePrivilegesRepository.findByAuthorityId(Mockito.anyLong())).willReturn(Collections.singletonList(rolePrivileges));

        given(this.roleRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Role.class)));

        List<RoleVO> roles = roleAuthorityService.roles("test");
        Assertions.assertNotNull(roles);
    }

    @Test
    void relation() {
        Role role = new Role();
        role.setId(1L);
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(role);

        Privilege privilege = new Privilege();
        privilege.setId(2L);
        given(this.privilegeRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(privilege);

        given(this.rolePrivilegesRepository.saveAllAndFlush(Mockito.anyCollection())).willReturn(Mockito.anyList());

        List<RolePrivileges> relation = roleAuthorityService.relation("test", Collections.singleton("test"));

        verify(this.rolePrivilegesRepository, times(1)).saveAllAndFlush(Mockito.anyList());
        Assertions.assertNotNull(relation);
    }
}