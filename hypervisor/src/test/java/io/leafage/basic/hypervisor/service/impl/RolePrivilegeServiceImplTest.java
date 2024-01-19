package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.repository.RolePrivilegesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

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
    private RolePrivilegesRepository rolePrivilegesRepository;

    @InjectMocks
    private RolePrivilegesServiceImpl roleAuthorityService;

    @Test
    void privileges() {
        given(this.rolePrivilegesRepository.findByRoleId(Mockito.anyLong())).willReturn(List.of(Mockito.mock(RolePrivileges.class)));

        List<RolePrivileges> privileges = roleAuthorityService.privileges(Mockito.anyLong());
        Assertions.assertNotNull(privileges);
    }

    @Test
    void roles() {
        given(this.rolePrivilegesRepository.findByPrivilegeId(Mockito.anyLong())).willReturn(List.of(Mockito.mock(RolePrivileges.class)));

        List<RolePrivileges> roles = roleAuthorityService.roles(Mockito.anyLong());
        Assertions.assertNotNull(roles);
    }

    @Test
    void relation() {
        given(this.rolePrivilegesRepository.saveAllAndFlush(Mockito.anyCollection())).willReturn(Mockito.anyList());

        List<RolePrivileges> relation = roleAuthorityService.relation(1L, Set.of(1L));

        verify(this.rolePrivilegesRepository, times(1)).saveAllAndFlush(Mockito.anyList());
        Assertions.assertNotNull(relation);
    }
}