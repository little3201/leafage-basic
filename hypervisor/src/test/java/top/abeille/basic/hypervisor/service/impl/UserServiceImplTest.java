/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.common.mock.AbstractServiceMock;

/**
 * 用户信息service测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
public class UserServiceImplTest extends AbstractServiceMock {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userInfoService;

    /**
     * 测试修改用户信息
     * 如果使用jpa的getOne(),必须加@Transactional，否则会曝出 hibernate lazyXXXXX - no session
     */
    @Test
    public void save() {
        UserDTO user = new UserDTO();
        user.setNickname("管理员");
        String pwd = new BCryptPasswordEncoder().encode("abeille");
        userInfoService.create(user);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

}