package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.service.UserGroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class GroupUserController {

    private static final Logger logger = LoggerFactory.getLogger(GroupUserController.class);

    private final UserGroupService userGroupService;

    public GroupUserController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    /**
     * 根据分组code查询关联用户信息
     *
     * @param code 组code
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/group/{code}/user")
    public ResponseEntity<Flux<UserVO>> groupRelation(@PathVariable String code) {
        Flux<UserVO> voFlux;
        try {
            voFlux = userGroupService.groupRelation(code);
        } catch (Exception e) {
            logger.error("Retrieve group users occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 根据username查询关联分组信息
     *
     * @param username 账号
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/user/{username}/group")
    public ResponseEntity<Flux<GroupVO>> userRelation(@PathVariable String username) {
        Flux<GroupVO> voFlux;
        try {
            voFlux = userGroupService.userRelation(username);
        } catch (Exception e) {
            logger.error("Retrieve user groups occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }
}
