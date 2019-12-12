/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.RoleInfoService;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.vo.UserDetailsVO;
import top.abeille.basic.hypervisor.vo.UserVO;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    private final UserInfoRepository userInfoRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleInfoService roleInfoService;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, UserRoleRepository userRoleRepository, RoleInfoService roleInfoService) {
        this.userInfoRepository = userInfoRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleInfoService = roleInfoService;
    }

    @Override
    public Mono<UserVO> create(UserDTO enter) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(enter, info);
        info.setModifier(0L);
        info.setModifyTime(LocalDateTime.now());
        return userInfoRepository.save(info).map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> modify(Long id, UserDTO s) {
        return null;
    }

    @Override
    public Mono<Void> removeById(Long userId) {
        return fetchByUserId(userId).flatMap(userInfo -> userInfoRepository.deleteById(userInfo.getId()));
    }

    @Override
    public Mono<UserDetailsVO> loadUserByUsername(String username) {
        UserInfo info = new UserInfo();
        info.setUsername(username);
        // 组装查询条件，只查询可用，未被锁定的用户信息
        ExampleMatcher exampleMatcher = appendConditions();
        Mono<UserVO> voMono = userInfoRepository.findOne(Example.of(info, exampleMatcher)).map(this::convertOuter);
        // 获取用户角色信息
        Flux<Long> authorities = voMono.map(userVO -> userRoleRepository.findAllByUserIdAndEnabled(userVO.getUserId(), true))
                .flatMapIterable(userRoles -> {
                    Set<Long> roleNameSet = new HashSet<>();
                    // 遍历获取角色信息
                    userRoles.forEach(userRole -> roleInfoService.queryById(userRole.getRoleId())
                            .map(roleOuter -> roleNameSet.add(roleOuter.getRoleId())));
                    return roleNameSet;
                });
        // 将结果装载
        return voMono.map(userVO -> {
            UserDetailsVO userDetailsVO = new UserDetailsVO();
            BeanUtils.copyProperties(userVO, userDetailsVO);
            userDetailsVO.setAuthorities(authorities);
            return userDetailsVO;
        });
    }

    @Override
    public Mono<UserVO> queryById(Long userId) {
        return fetchByUserId(userId).map(user -> {
            UserVO outer = new UserVO();
            BeanUtils.copyProperties(user, outer);
            return outer;
        });
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param userId 业务id
     * @return UserInfo 用户源数据
     */
    private Mono<UserInfo> fetchByUserId(Long userId) {
        ExampleMatcher exampleMatcher = appendConditions();
        UserInfo info = new UserInfo();
        info.setUserId(userId);
        this.appendParams(info);
        return userInfoRepository.findOne(Example.of(info, exampleMatcher));
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param info 用户信息
     * @return UserOuter 用户输出对象
     */
    private UserVO convertOuter(UserInfo info) {
        if (Objects.isNull(info)) {
            return null;
        }
        UserVO outer = new UserVO();
        BeanUtils.copyProperties(info, outer);
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
