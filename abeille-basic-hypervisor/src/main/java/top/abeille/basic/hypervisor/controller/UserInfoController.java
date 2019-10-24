/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.vo.UserDetailsVO;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 用户信息Controller
 *
 * @author liwenqiang 2018/8/2 21:02
 **/
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
    @GetMapping
    public ResponseEntity fetchUser(Integer pageNum, Integer pageSize) {
        Pageable pageable = super.initPageParams(pageNum, pageSize);
        Page<UserVO> users = userInfoService.fetchByPage(pageable);
        if (CollectionUtils.isEmpty(users.getContent())) {
            logger.info("Not found anything about user with pageable.");
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
    @GetMapping("/{userId}")
    public ResponseEntity queryUser(@PathVariable Long userId) {
        UserVO userVO = userInfoService.queryById(userId);
        if (Objects.isNull(userVO)) {
            logger.info("Not found anything about hypervisor with userId: {}.", userId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userVO);
    }

    /**
     * 用户查询——根据用户名
     *
     * @param username 用户名
     * @return ResponseEntity
     */
    @GetMapping("/load/{username}")
    public ResponseEntity loadUserByUsername(@PathVariable String username) {
        UserDetailsVO user = userInfoService.loadUserByUsername(username);
        if (user == null) {
            logger.info("Not found anything about user with username: {}.", username);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(user);
    }

    /**
     * 保存用户
     *
     * @param userDTO 用户
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity saveUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            userInfoService.save(userDTO);
        } catch (Exception e) {
            logger.error("Save user occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 编辑用户
     *
     * @param userDTO 用户
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity modifyUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            userInfoService.save(userDTO);
        } catch (Exception e) {
            logger.error("Modify user occurred an error: ", e);
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
    @DeleteMapping("/{userId}")
    public ResponseEntity removeUser(@PathVariable Long userId) {
        try {
            userInfoService.removeById(userId);
        } catch (Exception e) {
            logger.error("Remove user occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
