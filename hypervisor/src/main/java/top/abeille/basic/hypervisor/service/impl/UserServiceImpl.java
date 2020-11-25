/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.entity.RoleSource;
import top.abeille.basic.hypervisor.entity.SourceInfo;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.entity.UserRole;
import top.abeille.basic.hypervisor.repository.RoleSourceRepository;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.RoleService;
import top.abeille.basic.hypervisor.service.SourceService;
import top.abeille.basic.hypervisor.service.UserService;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * 用户信息service实现
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserServiceImpl extends AbstractBasicService implements UserService {

    /**
     * 开启日志
     */
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * email 正则
     */
    private static final String REGEX_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    /**
     * 手机号 正则
     */
    private static final String REGEX_MOBILE = "0?(13|14|15|17|18|19)[0-9]{9}";

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleSourceRepository roleSourceRepository;
    private final RoleService roleService;
    private final SourceService sourceService;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleSourceRepository roleSourceRepository, RoleService roleService, SourceService sourceService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleSourceRepository = roleSourceRepository;
        this.roleService = roleService;
        this.sourceService = sourceService;
    }

    @Override
    public Page<UserVO> retrieveByPage(Pageable pageable) {
        ExampleMatcher exampleMatcher = this.appendConditions();
        UserInfo userInfo = this.appendParams(new UserInfo());
        Page<UserInfo> infoPage = userRepository.findAll(Example.of(userInfo, exampleMatcher), pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(this::convertOuter);
    }

    @Override
    public UserVO create(UserDTO userDTO) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(userDTO, info);
        info.setBusinessId(PrefixEnum.US + this.generateId());
        info.setModifyTime(LocalDateTime.now());
        userRepository.save(info);
        return this.convertOuter(info);
    }

    @Override
    public UserVO modify(String businessId, UserDTO userDTO) {
        Optional<UserInfo> optional = this.fetchInfo(businessId);
        if (optional.isEmpty()) {
            log.info("修改对象未找到");
            return null;
        }
        UserInfo info = optional.get();
        info.setModifier(0L);
        userRepository.save(info);
        return this.convertOuter(info);
    }

    @Override
    public void removeById(String businessId) {
        Optional<UserInfo> optional = this.fetchInfo(businessId);
        if (optional.isPresent()) {
            UserInfo info = optional.get();
            userRepository.deleteById(info.getId());
        }
    }

    @Override
    public void removeInBatch(List<UserDTO> dtoList) {
    }

    @Override
    public UserVO fetchByBusinessId(String businessId) {
        UserInfo info = new UserInfo();
        info.setBusinessId(businessId);
        this.appendParams(info);
        Optional<UserInfo> optional = userRepository.findOne(Example.of(info));
        if (optional.isEmpty()) {
            return null;
        }
        return this.convertOuter(info);
    }

    @Override
    public UserDetails loadByUsername(String username) {
        UserInfo userInfo = this.initUser(username);
        Optional<UserInfo> optional = userRepository.findOne(Example.of(userInfo));
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("username does not exist");
        }
        userInfo = optional.get();
        UserRole ur = new UserRole();
        ur.setEnabled(Boolean.TRUE);
        ur.setUserId(userInfo.getId());
        List<UserRole> userRoles = userRoleRepository.findAll(Example.of(ur));
        // 检查角色是否配置
        if (CollectionUtils.isEmpty(userRoles)) {
            throw new InsufficientAuthenticationException("permission denied");
        }
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        userRoles.stream().map(userRole -> roleService.findById(userRole.getRoleId()))
                .forEach(roleInfo -> {
                    RoleSource roleSource = new RoleSource();
                    roleSource.setRoleId(roleInfo.getId());
                    // 查询角色资源
                    Optional<RoleSource> roleSourceOptional = roleSourceRepository.findOne(Example.of(roleSource));
                    if (roleSourceOptional.isPresent()) {
                        roleSource = roleSourceOptional.get();
                        // 查询资源信息
                        SourceInfo source = sourceService.findById(roleSource.getId());
                        // 添加到权限中
                        authorities.add(new SimpleGrantedAuthority(source.getBusinessId()));
                    }
                });
        return new User(isMobile(username) ? userInfo.getMobile() : userInfo.getEmail(), userInfo.getPassword(), userInfo.getEnabled(), userInfo.getAccountNonExpired(),
                userInfo.getCredentialsNonExpired(), userInfo.getAccountNonLocked(), authorities);
    }

    /**
     * 判断用户查询条件
     *
     * @return userInfo
     */
    private UserInfo initUser(String username) {
        // 判断是邮箱还是手机号
        UserInfo userInfo = new UserInfo();
        if (isMobile(username)) {
            userInfo.setMobile(username);
        } else if (isEmail(username)) {
            userInfo.setEmail(username);
        } else {
            log.error("{} 用户信息不存在", username);
            throw new UsernameNotFoundException(username);
        }
        // 根据登录名查找用户信息
        appendParams(userInfo);
        return userInfo;
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

    /**
     * 根据业务ID查信息
     *
     * @param businessId 业务ID
     * @return 数据库对象信息
     */
    private Optional<UserInfo> fetchInfo(String businessId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setBusinessId(businessId);
        this.appendParams(userInfo);
        return userRepository.findOne(Example.of(userInfo));
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param userInfo 用户信息
     * @return UserInfo
     */
    private UserInfo appendParams(UserInfo userInfo) {
        userInfo.setEnabled(true);
        userInfo.setAccountNonExpired(true);
        userInfo.setCredentialsNonExpired(true);
        return userInfo;
    }

    /**
     * 设置必要参数匹配条件
     *
     * @return ExampleMatcher
     */
    private ExampleMatcher appendConditions() {
        String[] fields = new String[]{"is_enabled", "is_credentials_non_expired", "is_account_non_locked", "is_account_non_expired"};
        ExampleMatcher matcher = ExampleMatcher.matching();
        for (String param : fields) {
            matcher.withMatcher(param, exact());
        }
        return matcher;
    }

    /**
     * 转换为输出对象
     *
     * @return ExampleMatcher
     */
    private UserVO convertOuter(UserInfo info) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(info, userVO);
        return userVO;
    }
}
