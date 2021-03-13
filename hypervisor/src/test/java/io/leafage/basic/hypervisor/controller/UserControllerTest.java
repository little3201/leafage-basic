package io.leafage.basic.hypervisor.controller;


import io.leafage.basic.hypervisor.repository.*;
import io.leafage.basic.hypervisor.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * 用户接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@SpringBootTest
public class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserRoleRepository userRoleRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private RoleAuthorityRepository roleAuthorityRepository;

    @MockBean
    private AuthorityRepository authorityRepository;

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private GroupUserRepository groupUserRepository;

    private final WebTestClient client = WebTestClient.bindToController(new UserController(
            new UserServiceImpl(userRepository, userRoleRepository, roleRepository, roleAuthorityRepository,
                    authorityRepository, groupRepository, groupUserRepository))).build();

    @Test
    void fetchDetails() {
        client.get().uri("/user/{username}", "little3201")
                .accept(MediaType.APPLICATION_JSON).exchange()
                .expectBody().jsonPath("authorities").isNotEmpty();
    }
}