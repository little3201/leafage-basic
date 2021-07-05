package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Group;
import io.leafage.basic.hypervisor.entity.User;
import io.leafage.basic.hypervisor.entity.UserGroup;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.repository.UserGroupRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.vo.GroupVO;
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
 * user group service测试
 *
 * @author liwenqiang 2021/7/5 17:36
 **/
@ExtendWith(MockitoExtension.class)
class UserGroupServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private UserGroupServiceImpl userGroupService;

    @Test
    void users() {
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Group.class));

        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(1L);
        userGroup.setGroupId(1L);
        given(this.userGroupRepository.findByGroupId(Mockito.anyLong())).willReturn(Collections.singletonList(userGroup));

        given(this.userRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(User.class)));

        List<UserVO> users = userGroupService.users("test");
        Assertions.assertNotNull(users);
    }

    @Test
    void groups() {
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(User.class));

        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(1L);
        userGroup.setGroupId(1L);
        given(this.userGroupRepository.findByUserId(Mockito.anyLong())).willReturn(Collections.singletonList(userGroup));

        given(this.groupRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Group.class)));

        List<GroupVO> groups = userGroupService.groups("test");
        Assertions.assertNotNull(groups);
    }

    @Test
    void relation() {
        User user = new User();
        user.setId(1L);
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(user);

        Group group = new Group();
        group.setId(2L);
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(group);

        given(this.userGroupRepository.saveAll(Mockito.anyCollection())).willReturn(Mockito.anyList());

        List<UserGroup> relation = userGroupService.relation("test", Collections.singleton("test"));
        Assertions.assertNotNull(relation);
    }
}