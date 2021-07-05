package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.entity.User;
import io.leafage.basic.hypervisor.entity.UserRole;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.basic.hypervisor.vo.UserVO;
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
 * user role service测试
 *
 * @author liwenqiang 2021/7/5 17:36
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
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Role.class));

        UserRole userRole = new UserRole();
        userRole.setUserId(1L);
        userRole.setRoleId(1L);
        given(this.userRoleRepository.findByRoleId(Mockito.anyLong())).willReturn(Collections.singletonList(userRole));

        given(this.userRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(User.class)));

        List<UserVO> users = userRoleService.users("test");
        Assertions.assertNotNull(users);
    }

    @Test
    void roles() {
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(User.class));

        UserRole userRole = new UserRole();
        userRole.setUserId(1L);
        userRole.setRoleId(1L);
        given(this.userRoleRepository.findByUserId(Mockito.anyLong())).willReturn(Collections.singletonList(userRole));

        given(this.roleRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Role.class)));

        List<RoleVO> roles = userRoleService.roles("test");
        Assertions.assertNotNull(roles);
    }

    @Test
    void relation() {
        User user = new User();
        user.setId(1L);
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(user);

        Role role = new Role();
        role.setId(2L);
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(role);

        given(this.userRoleRepository.saveAll(Mockito.anyCollection())).willReturn(Mockito.anyList());

        List<UserRole> relation = userRoleService.relation("test", Collections.singleton("test"));
        Assertions.assertNotNull(relation);
    }
}