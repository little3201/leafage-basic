/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.UserInfo;
import top.abeille.basic.hypervisor.document.UserRole;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.RoleResourceRepository;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.ResourceService;
import top.abeille.basic.hypervisor.service.RoleService;
import top.abeille.basic.hypervisor.service.UserService;
import top.abeille.basic.hypervisor.vo.UserDetailsVO;
import top.abeille.basic.hypervisor.vo.UserTidyVO;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractBasicService;

import javax.naming.NotContextException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * 用户信息service实现
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserServiceImpl extends AbstractBasicService implements UserService {

    /**
     * email 正则
     */
    private static final String REGEX_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    /**
     * 手机号 正则
     */
    private static final String REGEX_MOBILE = "0?(13|14|15|17|18|19)[0-9]{9}";
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;
    private final RoleResourceRepository roleResourceRepository;
    private final ResourceService resourceService;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleService roleService, RoleResourceRepository roleResourceRepository, ResourceService resourceService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleService = roleService;
        this.roleResourceRepository = roleResourceRepository;
        this.resourceService = resourceService;
    }

    @Override
    public Flux<UserVO> retrieveAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return userRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> create(UserDTO userDTO) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(userDTO, info);
        info.setUsername(info.getEmail().substring(0, info.getEmail().indexOf("@")));
        info.setPassword(new BCryptPasswordEncoder().encode("110119"));
        this.appendParams(info);
        info.setModifyTime(LocalDateTime.now());
        return userRepository.insert(info).doOnNext(userInfo -> {
            log.info("User :{} created.", userInfo.getUsername());
            if (!CollectionUtils.isEmpty(userDTO.getRoles())) {
                List<UserRole> userRoleList = userDTO.getRoles().stream().map(role ->
                        this.initUserRole(userInfo.getId(), role)).collect(Collectors.toList());
                userRoleRepository.saveAll(userRoleList).subscribe();
            }
        }).map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> modify(String username, UserDTO userDTO) {
        Asserts.notBlank(username, "username");
        return this.fetchInfo(username).flatMap(info -> {
            BeanUtils.copyProperties(userDTO, info);
            info.setModifyTime(LocalDateTime.now());
            return userRepository.save(info).doOnNext(userInfo -> {
                List<UserRole> userRoleList = userDTO.getRoles().stream().map(role ->
                        this.initUserRole(userInfo.getId(), role)).collect(Collectors.toList());
                userRoleRepository.saveAll(userRoleList).subscribe();
            }).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> remove(String username) {
        Asserts.notBlank(username, "username");
        return this.fetchInfo(username).flatMap(userInfo -> userRepository.deleteById(userInfo.getId()));
    }

    @Override
    public Mono<UserDetailsVO> fetchDetailsByUsername(String username) {
        Asserts.notBlank(username, "username");
        Mono<UserInfo> infoMono = userRepository.findOne(Example.of(this.convertCondition(username)))
                .switchIfEmpty(Mono.error(() -> new NotContextException("User Not Found")));
        Mono<ArrayList<String>> roleIdListMono = infoMono.flatMap(userInfo -> userRoleRepository.findByUserId(userInfo.getId())
                .switchIfEmpty(Mono.error(() -> new NotContextException("No Roles")))
                .collect(ArrayList::new, (roleIdList, userRole) -> roleIdList.add(userRole.getRoleId())));
        // 取角色关联的权限ID
        Mono<ArrayList<String>> sourceIdListMono = roleIdListMono.flatMap(roleIdList -> roleResourceRepository.findByRoleIdIn(roleIdList)
                .switchIfEmpty(Mono.error(() -> new NotContextException("No Authorities")))
                .collect(ArrayList::new, (sourceIdList, roleSource) -> sourceIdList.add(roleSource.getResourceId())));
        // 查权限
        Mono<Set<String>> authorityList = sourceIdListMono.flatMap(sourceIdList ->
                resourceService.findByIdInAndEnabledTrue(sourceIdList).collect(HashSet::new, (sourceList, sourceInfo) ->
                        sourceList.add(sourceInfo.getCode())));
        // 构造用户信息
        return authorityList.zipWith(infoMono, (authorities, userInfo) -> {
            UserDetailsVO detailsVO = new UserDetailsVO();
            BeanUtils.copyProperties(userInfo, detailsVO);
            detailsVO.setAuthorities(authorities);
            return detailsVO;
        });
    }

    @Override
    public Mono<UserTidyVO> fetchTidyByUsername(String username) {
        Asserts.notBlank(username, "username");
        return userRepository.findByUsername(username);
    }

    /**
     * 根据账号查信息
     *
     * @param username 账号
     * @return UserInfo 用户源数据
     */
    private Mono<UserInfo> fetchInfo(String username) {
        Asserts.notBlank(username, "username");
        UserInfo info = new UserInfo();
        info.setUsername(username);
        this.appendParams(info);
        return userRepository.findOne(Example.of(info));
    }

    /**
     * 数据脱敏
     *
     * @param info 信息
     * @return UserVO 输出对象
     */
    private UserVO convertOuter(UserInfo info) {
        UserVO outer = new UserVO();
        BeanUtils.copyProperties(info, outer);
        // 手机号脱敏
        if (StringUtils.isNotBlank(outer.getMobile())) {
            outer.setMobile(outer.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        }
        // 邮箱脱敏
        if (StringUtils.isNotBlank(outer.getEmail())) {
            outer.setEmail(outer.getEmail().replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2"));
        }
        return outer;
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param info 用户信息
     */
    private void appendParams(UserInfo info) {
        info.setAccountNonExpired(true);
        info.setAccountNonLocked(true);
        info.setCredentialsNonExpired(true);
    }

    /**
     * 初始设置UserRole参数
     *
     * @param userId   用户主键
     * @param roleCode 角色代码
     * @return 用户-角色对象
     */
    private UserRole initUserRole(String userId, String roleCode) {
        Asserts.notBlank(userId, "userId");
        Asserts.notBlank(roleCode, "roleCode");
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setModifier(userId);
        userRole.setModifyTime(LocalDateTime.now());
        roleService.findByCodeAndEnabledTrue(roleCode).doOnNext(roleInfo -> userRole.setRoleId(roleInfo.getId())).subscribe();
        return userRole;
    }

    /**
     * 根据username查询账户信息
     *
     * @param username 账号
     * @return 账户信息
     */
    private UserInfo convertCondition(String username) {
        UserInfo info = new UserInfo();
        // 判断是邮箱还是手机号
        if (isMobile(username)) {
            info.setMobile(username);
        } else if (isEmail(username)) {
            info.setEmail(username);
        } else {
            info.setUsername(username);
        }
        return info;
    }

    /**
     * 是否手机号
     *
     * @param mobile 匹配字符
     * @return true if mather otherwise false
     */
    private boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 是否邮箱
     *
     * @param email 匹配字符
     * @return true if mather, otherwise false
     */
    private boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }
}
