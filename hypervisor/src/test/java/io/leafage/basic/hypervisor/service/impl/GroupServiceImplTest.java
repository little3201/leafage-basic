package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.entity.Group;
import io.leafage.basic.hypervisor.entity.User;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import top.leafage.common.basic.TreeNode;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * group service测试
 *
 * @author liwenqiang 2021/5/11 10:10
 **/
@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    @Test
    void retrieve() {
        Group group = new Group();
        group.setName("test");
        Page<Group> page = new PageImpl<>(List.of(group));
        given(this.groupRepository.findByEnabledTrue(PageRequest.of(0, 2, Sort.by("id"))))
                .willReturn(page);

        Page<GroupVO> voPage = groupService.retrieve(0, 2, "id");
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void create() {
        User user = new User();
        user.setId(1L);
        user.setNickname("test");
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(user);

        Group group = new Group();
        group.setId(2L);
        group.setPrincipal(user.getId());
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(group);

        given(this.groupRepository.save(Mockito.any(Group.class))).willReturn(Mockito.mock(Group.class));

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setSuperior("2119JD09");
        groupDTO.setPrincipal(user.getNickname());
        GroupVO groupVO = groupService.create(groupDTO);

        verify(this.groupRepository, times(1)).save(Mockito.any(Group.class));
        Assertions.assertNotNull(groupVO);
    }

    @Test
    void modify() {
        Group group = new Group();
        group.setId(2L);
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(group);

        User user = new User();
        user.setId(1L);
        user.setNickname("test");
        given(this.userRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(user);

        given(this.groupRepository.save(Mockito.any(Group.class))).willReturn(Mockito.mock(Group.class));

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setSuperior("2119JD09");
        groupDTO.setPrincipal(user.getNickname());
        GroupVO groupVO = groupService.modify("", groupDTO);

        verify(this.groupRepository, times(1)).save(Mockito.any(Group.class));
        Assertions.assertNotNull(groupVO);
    }

    @Test
    void remove() {
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Group.class));

        groupService.remove("2119JD09");

        verify(this.groupRepository, times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void tree() {
        Group group = new Group();
        group.setId(1L);
        group.setCode("2119JD09");
        group.setName("test");

        Group child = new Group();
        child.setId(2L);
        child.setName("sub");
        child.setCode("2119JD19");
        child.setSuperior(1L);
        given(this.groupRepository.findByEnabledTrue()).willReturn(Arrays.asList(group, child));

        List<TreeNode> nodes = groupService.tree();
        Assertions.assertNotNull(nodes);
    }
}