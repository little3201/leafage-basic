/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.UserInfo;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.Arrays;
import java.util.Objects;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;


/**
 * 用户信息service实现
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserInfoServiceImpl extends AbstractBasicService implements UserInfoService {

    private final UserInfoRepository userInfoRepository;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public Mono<UserVO> create(UserDTO groupDTO) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(groupDTO, info);
        info.setBusinessId(this.getDateValue());
        return userInfoRepository.save(info).map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> modify(String businessId, UserDTO userDTO) {
        Objects.requireNonNull(businessId);
        return fetchByBusinessId(businessId).flatMap(articleInfo -> {
            BeanUtils.copyProperties(userDTO, articleInfo);
            return userInfoRepository.save(articleInfo).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchByBusinessId(businessId).flatMap(userInfo -> userInfoRepository.deleteById(userInfo.getId()));
    }

    @Override
    public Mono<UserVO> fetchById(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchByBusinessId(businessId).map(this::convertOuter);
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param businessId 业务id
     * @return UserInfo 用户源数据
     */
    private Mono<UserInfo> fetchByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        ExampleMatcher exampleMatcher = appendConditions();
        UserInfo info = new UserInfo();
        info.setBusinessId(businessId);
        this.appendParams(info);
        return userInfoRepository.findOne(Example.of(info, exampleMatcher));
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
        return outer;
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param info 用户信息
     */
    private void appendParams(UserInfo info) {
        info.setEnabled(Boolean.TRUE);
        info.setAccountNonExpired(Boolean.TRUE);
        info.setCredentialsNonExpired(Boolean.TRUE);
    }

    /**
     * 设置必要参数匹配条件
     *
     * @return ExampleMatcher
     */
    private ExampleMatcher appendConditions() {
        String[] fields = new String[]{"is_enabled", "is_credentials_non_expired", "is_account_non_locked", "is_account_non_expired"};
        ExampleMatcher matcher = ExampleMatcher.matching();
        Arrays.stream(fields).forEach(param -> matcher.withMatcher(param, exact()));
        return matcher;
    }
}
