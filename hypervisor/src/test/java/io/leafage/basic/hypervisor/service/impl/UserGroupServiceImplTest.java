package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Group;
import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.document.UserGroup;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.repository.UserGroupRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.BDDMockito.given;

/**
 * user-group接口测试
 *
 * @author liwenqiang 2021/6/14 11:10
 **/
@ExtendWith(MockitoExtension.class)
class UserGroupServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private UserGroupServiceImpl userGroupService;

    @Test
    void users() {
        Group group = new Group();
        ObjectId id = new ObjectId();
        group.setId(id);
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(group));

        UserGroup userGroup = new UserGroup();
        userGroup.setGroupId(id);
        userGroup.setUserId(new ObjectId());
        given(this.userGroupRepository.findByGroupIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Flux.just(userGroup));

        given(this.userRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(User.class)));
        StepVerifier.create(userGroupService.users("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void groups() {
        User user = new User();
        ObjectId id = new ObjectId();
        user.setId(id);
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(user));

        UserGroup userGroup = new UserGroup();
        userGroup.setGroupId(new ObjectId());
        userGroup.setUserId(id);
        given(this.userGroupRepository.findByUserIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Flux.just(userGroup));

        Group group = new Group();
        group.setCode("21612OL34");
        given(this.groupRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(group));
        StepVerifier.create(userGroupService.groups("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void relation() {
        User user = new User();
        ObjectId id = new ObjectId();
        user.setId(id);
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(user));

        Group group = new Group();
        group.setId(new ObjectId());
        given(this.groupRepository.findByCodeInAndEnabledTrue(Mockito.anyCollection())).willReturn(Flux.just(group));

        given(this.userGroupRepository.saveAll(Mockito.anyCollection())).willReturn(Flux.just(Mockito.mock(UserGroup.class)));

        StepVerifier.create(userGroupService.relation("little3201", Collections.singleton("21612OL34")))
                .expectNextCount(1).verifyComplete();
    }
}