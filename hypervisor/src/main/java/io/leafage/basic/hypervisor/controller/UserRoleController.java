package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.service.UserRoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class UserRoleController {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleController.class);

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    /**
     * 根据username查询关联角色信息
     *
     * @param username 用户username
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/user/{username}/role")
    public ResponseEntity<Flux<RoleVO>> userRelation(@PathVariable String username) {
        Flux<RoleVO> voFlux;
        try {
            voFlux = userRoleService.userRelation(username);
        } catch (Exception e) {
            logger.error("Retrieve user roles occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 根据code查询关联用户信息
     *
     * @param code 角色code
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/role/{code}/user")
    public ResponseEntity<Flux<UserVO>> roleRelation(@PathVariable String code) {
        Flux<UserVO> voFlux;
        try {
            voFlux = userRoleService.roleRelation(code);
        } catch (Exception e) {
            logger.error("Retrieve role users occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }
}
