package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.UserRole;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import io.leafage.basic.hypervisor.service.UserRoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.NotContextException;
import java.util.Set;

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
    public Flux<UserVO> users(String code) {
        return roleRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NotContextException::new))
                .flatMapMany(group -> userRoleRepository.findByRoleId(group.getId()).flatMap(userRole ->
                        userRepository.findById(userRole.getUserId()).map(user -> {
                            UserVO userVO = new UserVO();
                            BeanUtils.copyProperties(user, userVO);
                            return userVO;
                        }))
                );
    }

    @Override
    public Flux<RoleVO> roles(String username) {
        return userRepository.getByUsername(username).switchIfEmpty(Mono.error(NotContextException::new))
                .flatMapMany(user -> userRoleRepository.findByUserId(user.getId()).flatMap(userRole ->
                        roleRepository.findById(userRole.getRoleId()).map(role -> {
                            RoleVO roleVO = new RoleVO();
                            BeanUtils.copyProperties(role, roleVO);
                            return roleVO;
                        }))
                );
    }

    @Override
    public Flux<UserRole> relation(String username, Set<String> roles) {
        return userRepository.getByUsername(username).switchIfEmpty(Mono.error(NotContextException::new))
                .flatMapMany(user -> {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user.getId());
                    return roleRepository.findByCodeInAndEnabledTrue(roles).map(role -> {
                        userRole.setRoleId(role.getId());
                        return userRole;
                    });
                }).collectList().flatMapMany(userRoleRepository::saveAll);
    }
}
