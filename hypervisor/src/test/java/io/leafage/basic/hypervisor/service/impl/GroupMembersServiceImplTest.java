package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.GroupMembers;
import io.leafage.basic.hypervisor.repository.GroupMembersRepository;
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
 * user group service test
 *
 * @author liwenqiang 2021/7/5 17:36
 **/
@ExtendWith(MockitoExtension.class)
class GroupMembersServiceImplTest {

    @Mock
    private GroupMembersRepository groupMembersRepository;

    @InjectMocks
    private GroupMembersServiceImpl groupMembersService;

    @Test
    void members() {
        given(this.groupMembersRepository.findByGroupId(Mockito.anyLong())).willReturn(List.of(Mockito.mock(GroupMembers.class)));

        List<GroupMembers> accounts = groupMembersService.members(1L);
        Assertions.assertNotNull(accounts);
    }

    @Test
    void groups() {
        given(this.groupMembersRepository.findByUsername(Mockito.anyString())).willReturn(List.of(Mockito.mock(GroupMembers.class)));

        List<GroupMembers> groups = groupMembersService.groups("test");
        Assertions.assertNotNull(groups);
    }

    @Test
    void relation() {

        given(this.groupMembersRepository.saveAllAndFlush(Mockito.anyCollection())).willReturn(Mockito.anyList());

        List<GroupMembers> relation = groupMembersService.relation(1L, Set.of("test"));

        verify(this.groupMembersRepository, times(1)).saveAllAndFlush(Mockito.anyList());
        Assertions.assertNotNull(relation);
    }
}