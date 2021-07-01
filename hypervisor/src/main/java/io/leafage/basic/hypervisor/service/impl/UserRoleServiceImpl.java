package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.entity.User;
import io.leafage.basic.hypervisor.entity.UserRole;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import io.leafage.basic.hypervisor.service.UserRoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserRoleServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository,
                               RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserVO> users(String code) {
        Role role = roleRepository.getByCodeAndEnabledTrue(code);
        if (role == null) {
            return Collections.emptyList();
        }
        List<UserRole> userRoles = userRoleRepository.findByRoleId(role.getId());
        return userRoles.stream().map(userRole -> userRepository.findById(userRole.getUserId()))
                .map(user -> {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(user, userVO);
                    return userVO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<RoleVO> roles(String username) {
        Assert.hasText(username, "username is blank");
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        if (user == null) {
            return Collections.emptyList();
        }
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        if (CollectionUtils.isEmpty(userRoles)) {
            return Collections.emptyList();
        }
        return userRoles.stream().map(userRole -> roleRepository.findById(userRole.getRoleId()))
                .filter(Optional::isPresent).map(role -> {
                    RoleVO roleVO = new RoleVO();
                    BeanUtils.copyProperties(role, roleVO);
                    return roleVO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<UserRole> relation(String username, Set<String> roles) {
        Assert.hasText(username, "username is blank");
        Assert.notNull(roles, "roles is null");
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        if (user == null) {
            return Collections.emptyList();
        }
        List<UserRole> userRoles = roles.stream().map(s -> {
            Role role = roleRepository.getByCodeAndEnabledTrue(s);
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            return userRole;
        }).collect(Collectors.toList());
        return userRoleRepository.saveAll(userRoles);
    }
}
