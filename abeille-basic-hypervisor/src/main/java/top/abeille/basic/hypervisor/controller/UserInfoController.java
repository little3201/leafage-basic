/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.view.UserView;
import top.abeille.common.basic.AbstractController;

import javax.validation.constraints.NotNull;

/**
 * 用户信息Controller
 *
 * @author liwenqiang 2018/8/2 21:02
 **/
@Api(tags = "User Api")
@RestController
@RequestMapping("/user")
public class UserInfoController extends AbstractController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 用户查询——分页
     *
     * @param pageNum  当前页
     * @param pageSize 页内数据量
     * @return ResponseEntity
     */
    @ApiOperation(value = "Fetch enabled users with pageable")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity findUsers(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        Page<UserInfo> users = userInfoService.findAllByPage(pageNum, pageSize);
        if (CollectionUtils.isEmpty(users.getContent())) {
            log.info("Not found anything about hypervisor with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(users);
    }

    /**
     * 用户查询——根据ID
     *
     * @param userId 用户ID
     * @return ResponseEntity
     */
    @ApiOperation(value = "Get single hypervisor by userId")
    @ApiImplicitParam(name = "userId", required = true)
    @GetMapping("/{userId}")
    @JsonView(UserView.Details.class)
    public ResponseEntity getUser(@PathVariable String userId) {
        UserInfo user = userInfoService.getByUserId(userId);
        if (user == null) {
            log.info("Not found anything about hypervisor with userId: {}.", userId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(user);
    }

    /**
     * 保存用户
     *
     * @param user 用户
     * @return ResponseEntity
     */
    @ApiOperation(value = "Save single hypervisor")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity saveUser(@RequestBody UserInfo user) {
        try {
            userInfoService.save(user);
        } catch (Exception e) {
            log.error("Save hypervisor occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 编辑用户
     *
     * @param user 用户
     * @return ResponseEntity
     */
    @ApiOperation(value = "Modify single hypervisor")
    @PutMapping
    public ResponseEntity modifyUser(@RequestBody UserInfo user) {
        try {
            userInfoService.save(user);
        } catch (Exception e) {
            log.error("Modify hypervisor occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除用户——根据ID
     *
     * @param userId 用户ID
     * @return ResponseEntity
     */
    @ApiOperation(value = "Remove single hypervisor")
    @ApiImplicitParam(name = "userId", required = true)
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity removeUser(@PathVariable String userId) {
        try {
            userInfoService.removeByUserId(userId);
        } catch (Exception e) {
            log.error("Remove hypervisor occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
