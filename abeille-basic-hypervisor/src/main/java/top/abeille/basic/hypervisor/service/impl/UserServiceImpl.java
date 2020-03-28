/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.document.UserInfo;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.basic.hypervisor.service.UserService;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * 用户信息service实现
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserServiceImpl extends AbstractBasicService implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Flux<UserVO> retrieveAll(Sort sort) {
        return userRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> create(UserDTO groupDTO) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(groupDTO, info);
        info.setBusinessId(PrefixEnum.US + this.generateId());
        info.setPassword(new BCryptPasswordEncoder().encode("110119"));
        this.appendParams(info);
        info.setModifyTime(LocalDateTime.now());
        return userRepository.save(info).map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> modify(String businessId, UserDTO userDTO) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).flatMap(userInfo -> {
            BeanUtils.copyProperties(userDTO, userInfo);
            userInfo.setModifyTime(LocalDateTime.now());
            return userRepository.save(userInfo).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).flatMap(userInfo -> userRepository.deleteById(userInfo.getId()));
    }

    @Override
    public Mono<UserVO> fetchByBusinessId(String businessId) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).map(this::convertOuter);
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
        info.setAccountNonLocked(Boolean.TRUE);
        info.setCredentialsNonExpired(Boolean.TRUE);
    }

}
