/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.entity.UserRole;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.RoleInfoService;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.basic.hypervisor.vo.enter.UserEnter;
import top.abeille.basic.hypervisor.vo.outer.UserOuter;

import java.util.HashSet;
import java.util.List;
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
    public Mono<UserOuter> save(Long userId, UserEnter enter) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(enter, info);
        return userInfoRepository.save(info).map(user -> {
            UserOuter outer = new UserOuter();
            BeanUtils.copyProperties(user, outer);
            return outer;
        });
    }

    @Override
    public Mono<Void> removeById(Long userId) {
        return fetchByUserId(userId).flatMap(userInfo -> userInfoRepository.deleteById(userInfo.getId()));
    }

    @Override
    public Mono<UserVO> loadUserByUsername(String username) {
        UserInfo info = new UserInfo();
        info.setUsername(username);
        ExampleMatcher exampleMatcher = this.appendConditions();
        Mono<UserOuter> outerMono = userInfoRepository.findOne(Example.of(info, exampleMatcher)).map(user -> {
            UserOuter outer = new UserOuter();
            BeanUtils.copyProperties(user, outer);
            return outer;
        });
        //获取用户角色信息
        Mono<List<UserRole>> listMono = outerMono.map(userOuter -> userRoleRepository.findAllByUserIdAndEnabled(userOuter.getUserId(), true));
        //遍历用户角色信息，取出角色名
        Set<String> authorities = new HashSet<>();
        Flux<String> stringFlux = listMono.flatMapIterable(userRoles -> {
            userRoles.forEach(userRole -> roleInfoService.getByRoleId(userRole.getRoleId()).map(roleOuter -> authorities.add(roleOuter.getName())));
            return authorities;
        });
        return outerMono.map(userOuter -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(userOuter, userVO);
            userVO.setAuthorities(authorities);
            return userVO;
        });
    }

    @Override
    public Mono<UserOuter> getByUserId(Long userId) {
        return fetchByUserId(userId).map(user -> {
            UserOuter outer = new UserOuter();
            BeanUtils.copyProperties(user, outer);
            return outer;
        });
    }

    private Mono<UserInfo> fetchByUserId(Long userId) {
        ExampleMatcher exampleMatcher = this.appendConditions();
        UserInfo info = new UserInfo();
        info.setUserId(userId);
        this.appendParams(info);
        return userInfoRepository.findOne(Example.of(info, exampleMatcher));
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param info 用户信息
     */
    private void appendParams(UserInfo info) {
        info.setEnabled(true);
        info.setAccountNonExpired(true);
        info.setCredentialsNonExpired(true);
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
