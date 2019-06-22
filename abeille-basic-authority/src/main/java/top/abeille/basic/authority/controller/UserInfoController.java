/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.authority.entity.UserInfo;
import top.abeille.basic.authority.service.UserInfoService;
import top.abeille.basic.authority.view.UserView;
import top.abeille.common.basic.AbstractController;
import top.abeille.common.log.aop.LogServer;

/**
 * 用户信息Controller
 *
 * @author liwenqiang 2018/8/2 21:02
 **/
@Api(tags = "User Api")
@RestController
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
    @GetMapping("/users")
    public ResponseEntity findUsers(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        Page<UserInfo> users = userInfoService.findAllByPage(pageNum, pageSize);
        if (CollectionUtils.isEmpty(users.getContent())) {
            log.info("Not found anything about user with pageable.");
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
    @ApiOperation(value = "Get single user by userId")
    @ApiImplicitParam(name = "userId", required = true)
    @GetMapping("/user/{userId}")
    @JsonView(UserView.Details.class)
    public ResponseEntity getUser(@PathVariable String userId) {
        if (StringUtils.isBlank(userId)) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        UserInfo user = userInfoService.getById(userId);
        if (user == null) {
            log.info("Not found anything about user with userId: {}.", userId);
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
    @ApiOperation(value = "Save single user")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/user")
    @LogServer(value = "新增用户信息")
    public ResponseEntity saveUser(@RequestBody UserInfo user) {
        if (user.getUserId() == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        user.setModifierId(0L);
        try {
            userInfoService.save(user);
        } catch (Exception e) {
            log.error("Save user occurred an error: ", e);
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
    @ApiOperation(value = "Modify single user")
    @PutMapping("/user")
    public ResponseEntity modifyUser(@RequestBody UserInfo user) {
        if (user.getUserId() == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            userInfoService.save(user);
        } catch (Exception e) {
            log.error("Modify user occurred an error: ", e);
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
    @ApiOperation(value = "Remove single user")
    @ApiImplicitParam(name = "userId", required = true)
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity removeUser(@PathVariable String userId) {
        if (userId == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            userInfoService.removeById(userId);
        } catch (Exception e) {
            log.error("Remove user occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
