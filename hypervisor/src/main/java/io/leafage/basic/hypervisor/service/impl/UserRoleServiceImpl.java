package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Role;
import io.leafage.basic.hypervisor.document.UserRole;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import io.leafage.basic.hypervisor.service.UserRoleService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.NotContextException;
import java.util.NoSuchElementException;
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
        Assert.hasText(code, "code is blank");
        return roleRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(role -> userRoleRepository.findByRoleIdAndEnabledTrue(role.getId()).flatMap(userRole ->
                        userRepository.findById(userRole.getUserId()).map(user -> {
                            UserVO userVO = new UserVO();
                            BeanUtils.copyProperties(user, userVO);
                            return userVO;
                        })).switchIfEmpty(Mono.error(NoSuchElementException::new))
                );
    }

    @Override
    public Flux<String> roles(String username) {
        Assert.hasText(username, "username is blank");
        return userRepository.getByUsername(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(user -> userRoleRepository.findByUserIdAndEnabledTrue(user.getId()).flatMap(userRole ->
                                roleRepository.findById(userRole.getRoleId()).map(Role::getCode))
                        .switchIfEmpty(Mono.error(NoSuchElementException::new))
                );
    }

    @Override
    public Flux<UserRole> relation(String username, Set<String> roles) {
        Assert.hasText(username, "username is blank");
        Assert.notNull(roles, "roles is null");
        return userRepository.getByUsername(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(user -> {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user.getId());
                    return roleRepository.findByCodeInAndEnabledTrue(roles).switchIfEmpty(Mono.error(NotContextException::new))
                            .map(role -> {
                                userRole.setRoleId(role.getId());
                                return userRole;
                            }).switchIfEmpty(Mono.error(NoSuchElementException::new))
                            .collectList().flatMapMany(userRoleRepository::saveAll);
                });
    }
}
