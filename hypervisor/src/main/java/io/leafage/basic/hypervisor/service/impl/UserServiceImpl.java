/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;
import java.util.NoSuchElementException;


/**
 * 用户信息service 接口实现
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserServiceImpl extends AbstractBasicService implements UserService {

    private static final String MESSAGE = "username is blank.";

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Boolean> exist(String username) {
        return userRepository.existsByUsernameOrPhoneOrEmail(username, username, username);
    }

    @Override
    public Mono<Long> count() {
        return userRepository.count();
    }

    @Override
    public Mono<UserVO> create(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return userRepository.insert(user).map(this::convertOuter);
    }

    @Override
    public Mono<UserVO> modify(String username, UserDTO userDTO) {
        Assert.hasText(username, MESSAGE);
        return userRepository.getByUsername(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(user -> {
                    BeanUtils.copyProperties(userDTO, user);
                    return userRepository.save(user).map(this::convertOuter);
                });
    }

    @Override
    public Mono<Void> remove(String username) {
        Assert.hasText(username, MESSAGE);
        return userRepository.getByUsername(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(user -> userRepository.deleteById(user.getId()));
    }

    @Override
    public Mono<UserVO> fetch(String username) {
        Assert.hasText(username, MESSAGE);
        return userRepository.getByUsername(username).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .map(this::convertOuter);
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

}
