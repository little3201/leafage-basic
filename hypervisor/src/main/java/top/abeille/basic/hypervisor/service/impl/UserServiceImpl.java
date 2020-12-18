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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.User;
import top.abeille.basic.hypervisor.document.UserRole;
import top.abeille.basic.hypervisor.domain.UserDetails;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.*;
import top.abeille.basic.hypervisor.service.UserService;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractBasicService;

import javax.naming.NotContextException;
import java.util.*;


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
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setUsername(user.getEmail().substring(0, user.getEmail().indexOf("@")));
        user.setPassword(new BCryptPasswordEncoder().encode("110119"));
        Mono<User> userMono = userRepository.insert(user)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .doOnSuccess(u -> this.initUserRole(u.getId(), userDTO.getRoles()).subscribe(userRoles ->
                        userRoleRepository.saveAll(userRoles).subscribe()));
        return userMono.map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> modify(String username, UserDTO userDTO) {
        Asserts.notBlank(username, "username");
        Mono<User> userMono = userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(user -> {
                    BeanUtils.copyProperties(userDTO, user);
                    return userRepository.save(user)
                            .switchIfEmpty(Mono.error(RuntimeException::new))
                            .doOnSuccess(u ->
                                    this.initUserRole(u.getId(), userDTO.getRoles()).subscribe(userRoles ->
                                            userRoleRepository.saveAll(userRoles).subscribe()));
                });
        return userMono.map(this::convertOuter);
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
        Mono<ArrayList<String>> authorityIdListMono = roleIdListMono.flatMap(roleIdList -> roleAuthorityRepository.findByRoleIdIn(roleIdList)
                .switchIfEmpty(Mono.error(() -> new NotContextException("No Authorities")))
                .collect(ArrayList::new, (authorityIdList, roleAuthority) -> authorityIdList.add(roleAuthority.getAuthorityId())));
        // 查权限
        Mono<Set<String>> authorityCodeList = authorityIdListMono.flatMap(authorityIdList ->
                authorityRepository.findByIdInAndEnabledTrue(authorityIdList).collect(HashSet::new, (authorityList, authority) ->
                        authorityList.add(authority.getCode())));
        // 构造用户信息
        return authorityCodeList.zipWith(infoMono, (authorities, user) -> {
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
     * @param userId 用户主键
     * @param codes  角色代码
     * @return 用户-角色对象
     */
    private Mono<List<UserRole>> initUserRole(String userId, Collection<String> codes) {
        return roleRepository.findByCodeInAndEnabledTrue(codes).map(role -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setModifier(userId);
            userRole.setRoleId(role.getId());
            return userRole;
        }).collectList();
    }
}
