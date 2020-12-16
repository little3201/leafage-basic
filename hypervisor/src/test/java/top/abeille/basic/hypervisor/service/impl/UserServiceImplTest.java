/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.User;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.basic.hypervisor.vo.UserVO;

/**
 * 用户信息service测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * 测试修改用户信息
     * 如果使用jpa的getOne(),必须加@Transactional，否则会曝出 hibernate lazyXXXXX - no session
     */
    @Test
    public void save() {
        UserDTO userDTO = new UserDTO();
        userDTO.setNickname("管理员");
        String password = new BCryptPasswordEncoder().encode("123456");
        userService.create(userDTO);
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(Mockito.any());
    }

    /**
     * 测试根据业务ID查询用户信息, 正常返回数据
     */
    @Test
    public void fetchByUsername_returnObject() {
        String username = "little3201";
        Mockito.when(userRepository.findOne(Example.of(Mockito.any(User.class)))).thenReturn(Mockito.any());
        Mono<UserVO> userVOMono = userService.fetch(username);
        Assertions.assertNotNull(userVOMono.map(UserVO::getAuthorities).subscribe());
    }

    /**
     * 测试根据业务ID查询用户信息, 返回空数据
     */
    @Test
    public void fetchByUsername_returnEmpty() {
        String username = "little3201";
        Mockito.when(userRepository.findOne(Example.of(Mockito.any(User.class)))).thenReturn(Mockito.isNull());
        Mono<UserVO> userVOMono = userService.fetch(username);
        Assertions.assertNull(userVOMono.map(UserVO::getAuthorities).block());
    }

    @Test
    public void fetchDetails() {
        String username = "little3201";
        Mockito.when(userRepository.findByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username)).thenReturn(Mockito.any());
        Mono<UserVO> detailsMono = userService.fetch(username);
        Assertions.assertNotNull(detailsMono.map(UserVO::getAuthorities).subscribe());
    }
}