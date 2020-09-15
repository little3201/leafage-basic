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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.document.UserInfo;
import top.abeille.basic.hypervisor.document.UserRole;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.RoleSourceRepository;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.RoleService;
import top.abeille.basic.hypervisor.service.SourceService;
import top.abeille.basic.hypervisor.service.UserService;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final RoleSourceRepository roleSourceRepository;
    private final SourceService sourceService;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleService roleService, RoleSourceRepository roleSourceRepository, SourceService sourceService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleService = roleService;
        this.roleSourceRepository = roleSourceRepository;
        this.sourceService = sourceService;
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
        info.setBusinessId(PrefixEnum.US + this.generateId());
        info.setPassword(new BCryptPasswordEncoder().encode("110119"));
        this.appendParams(info);
        info.setModifyTime(LocalDateTime.now());
        return userRepository.insert(info).doOnNext(userInfo -> {
            List<UserRole> userRoleList = userDTO.getRoles().stream().map(role ->
                    this.initUserRole(userInfo.getId(), userInfo.getBusinessId(), role)).collect(Collectors.toList());
            userRoleRepository.saveAll(userRoleList).subscribe();
        }).map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> modify(String businessId, UserDTO userDTO) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchInfo(businessId).flatMap(info -> {
            BeanUtils.copyProperties(userDTO, info);
            return userRepository.save(info).doOnNext(userInfo -> {
                List<UserRole> userRoleList = userDTO.getRoles().stream().map(role ->
                        this.initUserRole(userInfo.getId(), userInfo.getBusinessId(), role)).collect(Collectors.toList());
                userRoleRepository.saveAll(userRoleList).subscribe();
            }).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).flatMap(userInfo -> userRepository.deleteById(userInfo.getId()));
    }

    @Override
    public Mono<UserVO> fetchByBusinessId(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchInfo(businessId).map(this::convertOuter);
    }

    @Override
    public Mono<UserDetails> loadByUsername(String username) {
        UserInfo info = this.findByUsername(username);
        Mono<UserInfo> infoMono = userRepository.findOne(Example.of(info));
        Mono<ArrayList<String>> roleIdListMono = infoMono.switchIfEmpty(Mono.error(() -> new UsernameNotFoundException("用户信息不存在")))
                .flatMap(userInfo -> userRoleRepository.findByUserId(userInfo.getId())
                        .collect(ArrayList::new, (roleIdList, userRole) -> roleIdList.add(userRole.getRoleId())));
        // 取角色关联的权限ID
        Mono<ArrayList<String>> sourceIdListMono = roleIdListMono.flatMap(roleIdList -> roleSourceRepository.findByRoleIdIn(roleIdList)
                .collect(ArrayList::new, (sourceIdList, roleSource) -> sourceIdList.add(roleSource.getSourceId())));
        // 查权限
        Mono<ArrayList<GrantedAuthority>> authorityList = sourceIdListMono.flatMap(sourceIdList -> sourceService.findByIdInAndEnabledTrue(sourceIdList)
                .collect(ArrayList::new, (sourceList, sourceInfo) -> sourceList.add(new SimpleGrantedAuthority(sourceInfo.getBusinessId()))));
        // 构造用户信息
        return authorityList.zipWith(infoMono, (authorities, userInfo) ->
                new User(StringUtils.isNotBlank(info.getMobile()) ? info.getMobile() : info.getEmail(), userInfo.getPassword(),
                        userInfo.getEnabled(), userInfo.getAccountNonExpired(), userInfo.getCredentialsNonExpired(),
                        userInfo.getAccountNonLocked(), authorities));
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param businessId 业务id
     * @return UserInfo 用户源数据
     */
    private Mono<UserInfo> fetchInfo(String businessId) {
        Objects.requireNonNull(businessId);
        UserInfo info = new UserInfo();
        info.setBusinessId(businessId);
        this.appendParams(info);
        return userRepository.findOne(Example.of(info));
    }

    /**
     * 设置查询条件的必要参数
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
        info.setEnabled(true);
        info.setAccountNonExpired(true);
        info.setAccountNonLocked(true);
        info.setCredentialsNonExpired(true);
    }

    /**
     * 初始设置UserRole参数
     *
     * @param userId         用户主键
     * @param modifier       用户业务ID
     * @param roleBusinessId 角色业务ID
     * @return 用户-角色对象
     */
    private UserRole initUserRole(String userId, String modifier, String roleBusinessId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setModifier(modifier);
        userRole.setModifyTime(LocalDateTime.now());
        roleService.fetchInfo(roleBusinessId).doOnNext(roleInfo -> userRole.setRoleId(roleInfo.getId())).subscribe();
        return userRole;
    }

    /**
     * 根据username查询账户信息
     *
     * @param username 账号
     * @return 账户信息
     */
    private UserInfo findByUsername(String username) {
        UserInfo info = new UserInfo();
        // 判断是邮箱还是手机号
        if (isMobile(username)) {
            info.setMobile(username);
        } else if (isEmail(username)) {
            info.setEmail(username);
        } else {
            log.error("账号错误：{}", username);
            throw new UsernameNotFoundException(username);
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
     * @return true if mather otherwise false
     */
    private boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }
}
