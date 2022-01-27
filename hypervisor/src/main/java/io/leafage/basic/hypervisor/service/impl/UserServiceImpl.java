/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.entity.User;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.leafage.common.basic.AbstractBasicService;

/**
 * 用户信息service实现.
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserServiceImpl extends AbstractBasicService implements UserService {

    private static final String MESSAGE = "username must not blank.";

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserVO create(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user = userRepository.save(user);
        return this.convertOuter(user);
    }

    @Override
    public UserVO modify(String username, UserDTO userDTO) {
        Assert.hasText(username, MESSAGE);
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        BeanUtils.copyProperties(userDTO, user);
        user = userRepository.saveAndFlush(user);
        return this.convertOuter(user);
    }

    @Override
    public void remove(String username) {
        Assert.hasText(username, MESSAGE);
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        user.setEnabled(false);
        userRepository.saveAndFlush(user);
    }

    @Override
    public UserVO fetch(String username) {
        Assert.hasText(username, MESSAGE);
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        return this.convertOuter(user);
    }

    @Override
    public boolean exist(String username) {
        return userRepository.existsByUsernameOrPhoneOrEmail(username, username, username);
    }

    /**
     * 转换为输出对象
     *
     * @return ExampleMatcher
     */
    private UserVO convertOuter(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

}
