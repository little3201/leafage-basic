/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.User;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * user service impl.
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserVO fetch(Long id) {
        Assert.notNull(id, "role id must not be null.");
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        return this.convertOuter(user);
    }

    @Override
    public boolean exist(String username) {
        Assert.hasText(username, "username must not be blank.");
        return userRepository.exists(username);
    }

    @Override
    public UserVO create(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user = userRepository.saveAndFlush(user);
        return this.convertOuter(user);
    }

    @Override
    public UserVO modify(Long id, UserDTO userDTO) {
        Assert.notNull(id, "role id must not be null.");
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        BeanUtils.copyProperties(userDTO, user);
        user = userRepository.save(user);
        return this.convertOuter(user);
    }

    @Override
    public void remove(Long id) {
        Assert.notNull(id, "role id must not be null.");
        userRepository.deleteById(id);
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
