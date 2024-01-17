package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.RoleMembers;
import io.leafage.basic.hypervisor.repository.RoleMembersRepository;
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
 * user role service test
 *
 * @author liwenqiang 2021/7/5 17:36
 **/
@ExtendWith(MockitoExtension.class)
class RoleMembersServiceImplTest {

    @Mock
    private RoleMembersRepository roleMembersRepository;

    @InjectMocks
    private RoleMembersServiceImpl roleMembersService;

    @Test
    void members() {
        given(this.roleMembersRepository.findByRoleId(Mockito.anyLong())).willReturn(List.of(Mockito.mock(RoleMembers.class)));

        List<RoleMembers> accounts = roleMembersService.members(Mockito.anyLong());
        Assertions.assertNotNull(accounts);
    }

    @Test
    void roles() {
        given(this.roleMembersRepository.findByUsername(Mockito.anyString())).willReturn(List.of(Mockito.mock(RoleMembers.class)));

        List<RoleMembers> roles = roleMembersService.roles(Mockito.anyString());
        Assertions.assertNotNull(roles);
    }

    @Test
    void relation() {
        given(this.roleMembersRepository.saveAllAndFlush(Mockito.anyIterable())).willReturn(Mockito.anyList());

        List<RoleMembers> relation = roleMembersService.relation(Mockito.anyLong(), Set.of(Mockito.anyString()));

        verify(this.roleMembersRepository, times(1)).saveAllAndFlush(Mockito.anyList());
        Assertions.assertNotNull(relation);
    }
}