/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.entity.RoleInfo;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.entity.UserRole;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.RoleInfoService;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.vo.UserVO;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * 用户信息service实现
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserInfoServiceImpl implements UserInfoService {

    /**
     * 开启日志
     */
    private static final Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    private final UserInfoRepository userInfoRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleInfoService roleInfoService;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, UserRoleRepository userRoleRepository, RoleInfoService roleInfoService) {
        this.userInfoRepository = userInfoRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleInfoService = roleInfoService;
    }

    @Override
    public Mono<UserInfo> getByExample(UserInfo userInfo) {
        /*Example对象可以当做查询条件处理，将查询条件得参数对应的属性进行设置即可
        可以通过ExampleMatcher.matching()方法进行进一步得处理*/
        ExampleMatcher exampleMatcher = this.appendConditions();
        this.appendParams(userInfo);
        return userInfoRepository.findOne(Example.of(userInfo, exampleMatcher));
    }

    @Override
    public Mono<UserInfo> save(UserInfo entity) {
        UserInfo example = this.getByUserId(entity.getUserId()).block();
        if (example != null) {
            entity.setId(example.getId());
        }
        if (entity.getModifier() == null) {
            entity.setModifier(0L);
        }
        return userInfoRepository.save(entity);
    }

    @Override
    public Mono<Void> removeById(Long id) {
        return userInfoRepository.deleteById(id);
    }

    @Override
    public UserVO loadUserByUsername(String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        UserInfo example = this.getByExample(userInfo).block();
        if (Objects.isNull(example)) {
            log.info("no user with username: {} be found", username);
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(example, userVO);
        List<UserRole> userRoles = userRoleRepository.findAllByUserIdAndEnabled(example.getId(), true);
        if (CollectionUtils.isEmpty(userRoles)) {
            log.info("the user with username: {} was unauthorized ", username);
            return null;
        }
        Set<String> authorities = new HashSet<>();
        userRoles.forEach(userRole -> {
            RoleInfo roleInfo = roleInfoService.getById(userRole.getRoleId()).block();
            if (roleInfo != null && StringUtils.isNotBlank(roleInfo.getName())) {
                authorities.add(roleInfo.getName().toUpperCase());
            }
        });
        userVO.setAuthorities(authorities);
        return userVO;
    }

    @Override
    public Mono<UserInfo> getByUserId(String userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        return this.getByExample(userInfo);
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
}
