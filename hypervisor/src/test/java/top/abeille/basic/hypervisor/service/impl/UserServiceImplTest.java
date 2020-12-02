/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.UserInfo;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.basic.hypervisor.vo.UserDetailsVO;
import top.abeille.basic.hypervisor.vo.UserTidyVO;

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
        userService.create(userDTO);
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(Mockito.any());
    }

    /**
     * 测试根据业务ID查询用户信息, 正常返回数据
     */
    @Test
    public void fetchByUsername_returnObject() {
        String username = "little3201";
        Mockito.when(userRepository.findOne(Example.of(Mockito.any(UserInfo.class)))).thenReturn(Mockito.any());
        Mono<UserTidyVO> userVOMono = userService.fetchTidy(username);
        Assert.assertNotNull(userVOMono.map(UserTidyVO::getNickname).subscribe());
    }

    /**
     * 测试根据业务ID查询用户信息, 返回空数据
     */
    @Test
    public void fetchByUsername_returnEmpty() {
        String username = "little3201";
        Mockito.when(userRepository.findOne(Example.of(Mockito.any(UserInfo.class)))).thenReturn(Mockito.isNull());
        Mono<UserTidyVO> userVOMono = userService.fetchTidy(username);
        Assert.assertNull(userVOMono.map(UserTidyVO::getNickname).block());
    }

    @Test
    public void loadByUsername() {
        String username = "little3201";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Mockito.any());
        Mono<UserDetailsVO> detailsMono = userService.fetchDetails(username);
        Assert.assertNotNull(detailsMono.map(UserDetailsVO::getAuthorities).subscribe());
    }
}