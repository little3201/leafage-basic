package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.repository.RolePrivilegesRepository;
import io.leafage.basic.hypervisor.service.RolePrivilegesService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

/**
 * role privileges service impl.
 *
 * @author liwenqiang 2021/9/27 14:18
 **/
@Service
public class RolePrivilegesServiceImpl implements RolePrivilegesService {

    private final RolePrivilegesRepository rolePrivilegesRepository;

    public RolePrivilegesServiceImpl(RolePrivilegesRepository rolePrivilegesRepository) {
        this.rolePrivilegesRepository = rolePrivilegesRepository;
    }

    @Override
    public List<RolePrivileges> privileges(Long roleId) {
        Assert.notNull(roleId, "role id must not be null.");

        return rolePrivilegesRepository.findByRoleId(roleId);
    }

    @Override
    public List<RolePrivileges> roles(Long privilegeId) {
        Assert.notNull(privilegeId, "privilege id must not be null.");

        return rolePrivilegesRepository.findByPrivilegeId(privilegeId);
    }

    @Override
    public List<RolePrivileges> relation(Long roleId, Set<Long> privilegeIds) {
        Assert.notNull(roleId, "role id must not be null.");
        Assert.notEmpty(privilegeIds, "privilege ids must not be empty.");

        List<RolePrivileges> rolePrivileges = privilegeIds.stream().map(privilegeId -> {
            RolePrivileges rolePrivilege = new RolePrivileges();
            rolePrivilege.setRoleId(roleId);
            rolePrivilege.setPrivilegeId(privilegeId);
            return rolePrivilege;
        }).toList();
        return rolePrivilegesRepository.saveAllAndFlush(rolePrivileges);
    }
}
