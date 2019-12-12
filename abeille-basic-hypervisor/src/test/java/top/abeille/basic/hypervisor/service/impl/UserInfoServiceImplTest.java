/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;

/**
 * 用户信息service测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@RunWith(MockitoJUnitRunner.class)
public class UserInfoServiceImplTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    /**
     * 测试修改用户信息
     * 如果使用jpa的getOne(),必须加@Transactional，否则会曝出 hibernate lazyXXXXX - no session
     */
    @Test
    public void save() {
        UserDTO user = new UserDTO();
        user.setNickname("管理员");
        String pwd = new BCryptPasswordEncoder().encode("abeille");
        user.setPassword(pwd);
        userInfoService.create(user);
    }

    /**
     * 条件查询用户信息
     * 如果使用jpa的findOne()
     */
    @Test
    public void getByExample() {

    }
}