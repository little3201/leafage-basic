/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.GroupUser;
import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.document.UserRole;
import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.*;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import io.leafage.common.basic.AbstractBasicService;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.NotContextException;
import java.util.*;


/**
 * 用户信息service实现
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserServiceImpl extends AbstractBasicService implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final AuthorityRepository authorityRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository,
                           RoleRepository roleRepository, RoleAuthorityRepository roleAuthorityRepository,
                           AuthorityRepository authorityRepository, GroupRepository groupRepository,
                           GroupUserRepository groupUserRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.authorityRepository = authorityRepository;
        this.groupRepository = groupRepository;
        this.groupUserRepository = groupUserRepository;
    }

    @Override
    public Flux<UserVO> retrieve(int page, int size) {
        return userRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public Flux<UserVO> relation(String code) {
        return groupRepository.getByCodeAndEnabledTrue(code).flatMapMany(group ->
                groupUserRepository.findByGroupId(group.getId()).flatMap(groupUser ->
                        userRepository.findById(groupUser.getUserId()).map(this::convertOuter)
                )
        );
    }

    @Override
    public Mono<Long> count() {
        return userRepository.count();
    }

    @Override
    public Mono<UserVO> create(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        Mono<User> userMono = userRepository.insert(user)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                // 处理roles
                .doOnSuccess(u -> this.initUserRole(u.getId(), userDTO.getRoles()).doOnNext(userRoles ->
                        userRoleRepository.saveAll(userRoles).subscribe()))
                // 处理groups
                .doOnSuccess(info -> this.initGroupUser(info.getId(), userDTO.getGroups()).doOnNext(groupUsers ->
                        groupUserRepository.saveAll(groupUsers).subscribe()));
        return userMono.map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> modify(String username, UserDTO userDTO) {
        Assert.hasText(username, "username is blank");
        Mono<User> userMono = userRepository.getByUsername(username)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(user -> {
                    BeanUtils.copyProperties(userDTO, user);
                    return userRepository.save(user)
                            .switchIfEmpty(Mono.error(RuntimeException::new))
                            // 处理roles
                            .doOnSuccess(u -> this.initUserRole(u.getId(), userDTO.getRoles()).subscribe(userRoles ->
                                    userRoleRepository.saveAll(userRoles).subscribe()))
                            // 处理groups
                            .doOnSuccess(info -> this.initGroupUser(info.getId(), userDTO.getGroups()).doOnNext(groupUsers ->
                                    groupUserRepository.saveAll(groupUsers).subscribe()));
                });
        return userMono.map(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(String username) {
        Assert.hasText(username, "username is blank");
        return userRepository.getByUsername(username).flatMap(user -> userRepository.deleteById(user.getId()));
    }

    @Override
    public Mono<UserVO> fetch(String username) {
        Assert.hasText(username, "username is blank");
        return userRepository.getByUsername(username).map(this::convertOuter);
    }

    @Override
    public Mono<UserDetails> fetchDetails(String username) {
        Assert.hasText(username, "username is blank");
        Mono<User> infoMono = userRepository.getByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username)
                .switchIfEmpty(Mono.error(() -> new NotContextException("User Not Found")));
        Mono<ArrayList<ObjectId>> roleIdListMono = infoMono.flatMap(user -> userRoleRepository.findByUserIdAndEnabledTrue(user.getId())
                .switchIfEmpty(Mono.error(() -> new NotContextException("No Roles")))
                .collect(ArrayList::new, (roleIdList, userRole) -> roleIdList.add(userRole.getRoleId())));
        // 取角色关联的权限ID
        Mono<ArrayList<ObjectId>> authorityIdListMono = roleIdListMono.flatMap(roleIdList -> roleAuthorityRepository.findByRoleIdIn(roleIdList)
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
     * @param codes  role
     * @return 用户-角色对象
     */
    private Mono<List<UserRole>> initUserRole(ObjectId userId, Collection<String> codes) {
        return roleRepository.findByCodeInAndEnabledTrue(codes).map(role -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(role.getId());
            userRole.setModifier(userId);
            return userRole;
        }).collectList();
    }

    /**
     * 初始设置GroupUser参数
     *
     * @param userId 用户主键
     * @param codes  group
     * @return 用户-角色对象
     */
    private Mono<List<GroupUser>> initGroupUser(ObjectId userId, Collection<String> codes) {
        return groupRepository.findByCodeInAndEnabledTrue(codes).map(group -> {
            GroupUser groupUser = new GroupUser();
            groupUser.setUserId(userId);
            groupUser.setGroupId(group.getId());
            groupUser.setModifier(userId);
            return groupUser;
        }).collectList();
    }
}
