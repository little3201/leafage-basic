package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.GroupRoles;
import io.leafage.basic.hypervisor.repository.GroupRolesRepository;
import io.leafage.basic.hypervisor.service.GroupRolesService;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

/**
 * group roles service impl.
 *
 * @author liwenqiang 2024/2/2 15:20
 **/
public class GroupRolesServiceImpl implements GroupRolesService {

    private final GroupRolesRepository groupRolesRepository;

    public GroupRolesServiceImpl(GroupRolesRepository groupRolesRepository) {
        this.groupRolesRepository = groupRolesRepository;
    }

    @Override
    public List<GroupRoles> roles(Long groupId) {
        return groupRolesRepository.findByGroupId(groupId);
    }

    @Override
    public List<GroupRoles> groups(Long roleId) {
        return groupRolesRepository.findByRoleId(roleId);
    }

    @Override
    public List<GroupRoles> relation(Long groupId, Set<Long> roleIds) {
        Assert.notNull(groupId, "group id must not be null.");
        Assert.notEmpty(roleIds, "role ids must not be empty.");

        List<GroupRoles> groupRoles = roleIds.stream().map(roleId -> {
            GroupRoles groupRole = new GroupRoles();
            groupRole.setGroupId(groupId);
            groupRole.setRoleId(roleId);
            return groupRole;
        }).toList();
        return groupRolesRepository.saveAllAndFlush(groupRoles);
    }
}
