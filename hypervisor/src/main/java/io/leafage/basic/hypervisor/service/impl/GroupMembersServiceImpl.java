package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.GroupMembers;
import io.leafage.basic.hypervisor.repository.GroupMembersRepository;
import io.leafage.basic.hypervisor.service.GroupMembersService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

/**
 * account group service impl.
 *
 * @author liwenqiang 2021/11/27 14:18
 **/
@Service
public class GroupMembersServiceImpl implements GroupMembersService {

    private final GroupMembersRepository groupMembersRepository;

    public GroupMembersServiceImpl(GroupMembersRepository groupMembersRepository) {
        this.groupMembersRepository = groupMembersRepository;
    }

    @Override
    public List<GroupMembers> members(Long groupId) {
        Assert.notNull(groupId, "group id must not be null.");

        return groupMembersRepository.findByGroupId(groupId);
    }

    @Override
    public List<GroupMembers> groups(String username) {
        Assert.hasText(username, "username must not be blank.");

        return groupMembersRepository.findByUsername(username);
    }

    @Override
    public List<GroupMembers> relation(Long groupId, Set<String> usernames) {
        Assert.notNull(groupId, "group id must not be null.");
        Assert.notEmpty(usernames, "usernames must not be empty.");

        List<GroupMembers> groupMembers = usernames.stream().map(username -> {
            GroupMembers members = new GroupMembers();
            members.setGroupId(groupId);
            members.setUsername(username);
            return members;
        }).toList();
        return groupMembersRepository.saveAllAndFlush(groupMembers);
    }
}
