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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;


/**
 * user service impl
 *
 * @author wq li 2018-07-28 0:30
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * <p>Constructor for UserServiceImpl.</p>
     *
     * @param userRepository a {@link io.leafage.basic.hypervisor.repository.UserRepository} object
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Page<UserVO>> retrieve(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Flux<UserVO> voFlux = userRepository.findByEnabledTrue(pageable).flatMap(this::convertOuter);

        Mono<Long> count = userRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<UserVO> fetch(Long id) {
        Assert.notNull(id, "user id must not be blank.");
        return userRepository.findById(id).flatMap(this::convertOuter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> exist(String username) {
        Assert.hasText(username, "username must not be blank.");
        return userRepository.existsByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<UserVO> create(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword("123456");
        return userRepository.save(user).flatMap(this::convertOuter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<UserVO> modify(Long id, UserDTO userDTO) {
        Assert.notNull(id, "user id must not be blank.");
        return userRepository.findById(id).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .doOnNext(user -> BeanUtils.copyProperties(userDTO, user))
                .flatMap(userRepository::save)
                .flatMap(this::convertOuter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, "user id must not be blank.");
        return userRepository.deleteById(id);
    }


    /**
     * 数据转换
     *
     * @param user 信息
     * @return UserVO 输出对象
     */
    private Mono<UserVO> convertOuter(User user) {
        return Mono.just(user).map(u -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(u, vo);
            vo.setLastModifiedDate(u.getLastModifiedDate().orElse(null));
            return vo;
        });
    }

}
