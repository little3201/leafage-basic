package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Group;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.repository.GroupMembersRepository;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import top.leafage.common.TreeNode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * group service test
 *
 * @author wq li 2021/5/11 10:10
 **/
@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupMembersRepository groupMembersRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    private GroupDTO groupDTO;

    @BeforeEach
    void init() {
        groupDTO = new GroupDTO();
        groupDTO.setName("group");
        groupDTO.setPrincipal("test");
        groupDTO.setSuperiorId(1L);
        groupDTO.setDescription("group");
        groupDTO.setSuperiorId(1L);
    }

    @Test
    void retrieve() {
        Page<Group> page = new PageImpl<>(List.of(Mockito.mock(Group.class)));
        given(this.groupRepository.findAll(PageRequest.of(0, 2, Sort.by("id"))))
                .willReturn(page);

        given(this.groupMembersRepository.countByGroupIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.anyLong());

        Page<GroupVO> voPage = groupService.retrieve(0, 2, "id");
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void create() {
        given(this.groupRepository.saveAndFlush(Mockito.any(Group.class))).willReturn(Mockito.mock(Group.class));

        given(this.groupMembersRepository.countByGroupIdAndEnabledTrue(Mockito.anyLong())).willReturn(2L);

        GroupVO groupVO = groupService.create(Mockito.mock(GroupDTO.class));

        verify(this.groupRepository, times(1)).saveAndFlush(Mockito.any(Group.class));
        Assertions.assertNotNull(groupVO);
    }

    @Test
    void create_error() {
        given(this.groupRepository.saveAndFlush(Mockito.any(Group.class))).willThrow(new RuntimeException());

        Assertions.assertThrows(RuntimeException.class, () -> groupService.create(Mockito.mock(GroupDTO.class)));
    }

    @Test
    void modify() {
        given(this.groupRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Group.class)));

        given(this.groupRepository.save(Mockito.any(Group.class))).willReturn(Mockito.mock(Group.class));

        given(this.groupMembersRepository.countByGroupIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.anyLong());

        GroupVO groupVO = groupService.modify(1L, groupDTO);

        verify(this.groupRepository, times(1)).save(Mockito.any(Group.class));
        Assertions.assertNotNull(groupVO);
    }

    @Test
    void remove() {
        groupService.remove(1L);

        verify(this.groupRepository, times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void tree() {
        given(this.groupRepository.findByEnabledTrue()).willReturn(Arrays.asList(Mockito.mock(Group.class), Mockito.mock(Group.class)));

        List<TreeNode> nodes = groupService.tree();
        Assertions.assertNotNull(nodes);
    }
}