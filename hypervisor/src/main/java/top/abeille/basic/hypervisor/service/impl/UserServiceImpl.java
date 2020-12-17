/*
 * Copyright (c) 2020. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.Role;
import top.abeille.basic.hypervisor.document.User;
import top.abeille.basic.hypervisor.document.UserRole;
import top.abeille.basic.hypervisor.domain.UserDetails;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.*;
import top.abeille.basic.hypervisor.service.UserService;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractBasicService;

import javax.naming.NotContextException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 用户信息service实现
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserServiceImpl extends AbstractBasicService implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository,
                           RoleRepository roleRepository, RoleAuthorityRepository roleAuthorityRepository,
                           AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Flux<UserVO> retrieve(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return userRepository.findByEnabledTrue(PageRequest.of(page, size, sort)).map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> create(UserDTO userDTO) {
        User info = new User();
        BeanUtils.copyProperties(userDTO, info);
        info.setUsername(info.getEmail().substring(0, info.getEmail().indexOf("@")));
        info.setPassword(new BCryptPasswordEncoder().encode("110119"));
        return userRepository.insert(info).doOnNext(user -> {
            log.info("User :{} created.", user.getUsername());
            if (!CollectionUtils.isEmpty(userDTO.getRoles())) {
                List<UserRole> userRoleList = userDTO.getRoles().stream().map(role ->
                        this.initUserRole(user.getId(), role)).collect(Collectors.toList());
                userRoleRepository.saveAll(userRoleList).subscribe();
            }
        }).map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> modify(String username, UserDTO userDTO) {
        Asserts.notBlank(username, "username");
        return userRepository.findByUsername(username).flatMap(info -> {
            BeanUtils.copyProperties(userDTO, info);
            return userRepository.save(info).doOnNext(user -> {
                List<UserRole> userRoleList = userDTO.getRoles().stream().map(code ->
                        this.initUserRole(user.getId(), code)).collect(Collectors.toList());
                userRoleRepository.saveAll(userRoleList).subscribe();
            }).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> remove(String username) {
        Asserts.notBlank(username, "username");
        return userRepository.findByUsername(username).flatMap(user -> userRepository.deleteById(user.getId()));
    }

    @Override
    public Mono<UserDetails> fetchDetails(String username) {
        Asserts.notBlank(username, "username");
        Mono<User> infoMono = userRepository.findByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username)
                .switchIfEmpty(Mono.error(() -> new NotContextException("User Not Found")));
        Mono<ArrayList<String>> roleIdListMono = infoMono.flatMap(user -> userRoleRepository.findByUserId(user.getId())
                .switchIfEmpty(Mono.error(() -> new NotContextException("No Roles")))
                .collect(ArrayList::new, (roleIdList, userRole) -> roleIdList.add(userRole.getRoleId())));
        // 取角色关联的权限ID
        Mono<ArrayList<String>> sourceIdListMono = roleIdListMono.flatMap(roleIdList -> roleAuthorityRepository.findByRoleIdIn(roleIdList)
                .switchIfEmpty(Mono.error(() -> new NotContextException("No Authorities")))
                .collect(ArrayList::new, (sourceIdList, roleSource) -> sourceIdList.add(roleSource.getAuthorityId())));
        // 查权限
        Mono<Set<String>> authorityList = sourceIdListMono.flatMap(sourceIdList ->
                authorityRepository.findByIdInAndEnabledTrue(sourceIdList).collect(HashSet::new, (sourceList, sourceInfo) ->
                        sourceList.add(sourceInfo.getCode())));
        // 构造用户信息
        return authorityList.zipWith(infoMono, (authorities, user) -> {
            UserDetails userDetails = new UserDetails();
            BeanUtils.copyProperties(user, userDetails);
            userDetails.setAuthorities(authorities);
            return userDetails;
        });
    }

    /**
     * 数据脱敏
     *
     * @param info 信息
     * @return UserVO 输出对象
     */
    private UserVO convertOuter(User info) {
        UserVO outer = new UserVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }

    /**
     * 初始设置UserRole参数
     *
     * @param userId   用户主键
     * @param roleCode 角色代码
     * @return 用户-角色对象
     */
    private UserRole initUserRole(String userId, String roleCode) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setModifier(userId);
        Role role = roleRepository.findByCodeAndEnabledTrue(roleCode).block();
        if (role != null) {
            userRole.setRoleId(role.getId());
        }
        return userRole;
    }
}
