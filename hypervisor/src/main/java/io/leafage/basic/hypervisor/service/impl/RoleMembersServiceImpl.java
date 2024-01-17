package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.RoleMembers;
import io.leafage.basic.hypervisor.repository.RoleMembersRepository;
import io.leafage.basic.hypervisor.service.RoleMembersService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

/**
 * role members service impl.
 *
 * @author liwenqiang 2021/11/27 14:18
 **/
@Service
public class RoleMembersServiceImpl implements RoleMembersService {

    private final RoleMembersRepository roleMembersRepository;

    public RoleMembersServiceImpl(RoleMembersRepository roleMembersRepository) {
        this.roleMembersRepository = roleMembersRepository;
    }

    @Override
    public List<RoleMembers> members(Long roleId) {
        Assert.notNull(roleId, "role id must not be null.");

        return roleMembersRepository.findByRoleId(roleId);
    }

    @Override
    public List<RoleMembers> roles(String username) {
        Assert.hasText(username, "username must not be blank.");

        return roleMembersRepository.findByUsername(username);
    }

    @Override
    public List<RoleMembers> relation(Long roleId, Set<String> usernames) {
        Assert.notNull(roleId, "role id must not be null.");
        Assert.notEmpty(usernames, "usernames must not be empty.");

        List<RoleMembers> roleMembers = usernames.stream().map(username -> {
            RoleMembers members = new RoleMembers();
            members.setRoleId(roleId);
            members.setUsername(username);
            return members;
        }).toList();
        return roleMembersRepository.saveAllAndFlush(roleMembers);
    }
}
