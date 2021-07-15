package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.document.UserRole;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.service.UserGroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import static org.mockito.BDDMockito.given;

/**
 * group接口测试类
 *
 * @author liwenqiang 2021/6/19 10:00
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserGroupService userGroupService;

    @MockBean
    private GroupService groupService;

    @Test
    void retrieve() {
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
        given(this.groupService.retrieve(0, 2)).willReturn(Flux.just(groupVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/group").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void tree() {
        TreeNode treeNode = new TreeNode("21612OL34", "test");
        given(this.groupService.tree()).willReturn(Flux.just(treeNode));

        webTestClient.get().uri("/group/tree").exchange()
                .expectStatus().isOk().expectBodyList(TreeNode.class);
    }

    @Test
    void fetch() {
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
        given(this.groupService.fetch(Mockito.anyString())).willReturn(Mono.just(groupVO));

        webTestClient.get().uri("/group/{code}", "21612OL34").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void count() {
        given(this.groupService.count()).willReturn(Mono.just(2L));

        webTestClient.get().uri("/group/count").exchange().expectStatus().isOk();
    }

    @Test
    void create() {
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
        given(this.groupService.create(Mockito.any(GroupDTO.class))).willReturn(Mono.just(groupVO));

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        webTestClient.post().uri("/group").bodyValue(groupDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void modify() {
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
        given(this.groupService.modify(Mockito.anyString(), Mockito.any(GroupDTO.class))).willReturn(Mono.just(groupVO));

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        webTestClient.put().uri("/group/{code}", "21612OL34").bodyValue(groupDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void remove() {
        given(this.groupService.remove(Mockito.anyString())).willReturn(Mono.empty());

        webTestClient.delete().uri("/group/{code}", "21612OL34").exchange()
                .expectStatus().isOk();
    }

    @Test
    void users() {
        UserVO userVO = new UserVO();
        userVO.setUsername("little3201");
        given(this.userGroupService.users(Mockito.anyString())).willReturn(Flux.just(userVO));

        webTestClient.get().uri("/group/{code}/user", "21612OL34").exchange()
                .expectStatus().isOk()
                .expectBodyList(UserRole.class);
    }
}