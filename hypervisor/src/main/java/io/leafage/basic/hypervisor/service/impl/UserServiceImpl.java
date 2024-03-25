/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.User;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Optional;

/**
 * user service impl.
 *
 * @author wq li 2018/7/28 0:30
 **/
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "lastModifiedDate"));
        return userRepository.findAll(pageable).map(this::convertOuter);
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
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserVO create(UserDTO dto) {
        User user = new User();
        BeanCopier copier = BeanCopier.create(UserDTO.class, User.class, false);
        copier.copy(dto, user, null);

        user = userRepository.saveAndFlush(user);
        return this.convertOuter(user);
    }

    @Override
    public UserVO modify(Long id, UserDTO dto) {
        Assert.notNull(id, "role id must not be null.");
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        BeanCopier copier = BeanCopier.create(UserDTO.class, User.class, false);
        copier.copy(dto, user, null);

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
        UserVO vo = new UserVO();
        BeanCopier copier = BeanCopier.create(User.class, UserVO.class, false);
        copier.copy(user, vo, null);

        // get lastModifiedDate
        Optional<Instant> optionalInstant = user.getLastModifiedDate();
        optionalInstant.ifPresent(vo::setLastModifiedDate);
        return vo;
    }

}
