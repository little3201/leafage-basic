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
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 用户信息接口
 *
 * @author liwenqiang 2018/8/2 21:02
 * @version 0.0.1
 * @since 1.0
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
     * @since 1.0
     */
    @GetMapping
    public ResponseEntity<Object> retrieveUser(Integer pageNum, Integer pageSize) {
        Pageable pageable = super.initPageParams(pageNum, pageSize);
        Page<UserVO> users = userInfoService.retrieveByPage(pageable);
        if (CollectionUtils.isEmpty(users.getContent())) {
            logger.info("Not found anything about user with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(users);
    }

    /**
     * 用户查询——根据ID
     *
     * @param businessId 业务ID
     * @return ResponseEntity
     * @since 1.0
     */
    @GetMapping("/{businessId}")
    public ResponseEntity<Object> fetchUser(@PathVariable String businessId) {
        UserVO userVO = userInfoService.fetchByBusinessId(businessId);
        if (Objects.isNull(userVO)) {
            logger.info("Not found anything about hypervisor with userId: {}.", businessId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userVO);
    }

    /**
     * 添加用户
     *
     * @param userDTO 用户
     * @return ResponseEntity
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            userInfoService.create(userDTO);
        } catch (Exception e) {
            logger.error("Save user occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 修改用户
     *
     * @param businessId 业务ID
     * @param userDTO    用户
     * @return ResponseEntity
     * @since 1.0
     */
    @PutMapping("/{businessId}")
    public ResponseEntity<Object> modifyUser(@PathVariable String businessId, @RequestBody @Valid UserDTO userDTO) {
        try {
            userInfoService.modify(businessId, userDTO);
        } catch (Exception e) {
            logger.error("Modify user occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除用户——根据ID
     *
     * @param businessId 用户ID
     * @return ResponseEntity
     * @since 1.0
     */
    @DeleteMapping("/{businessId}")
    public ResponseEntity<Object> removeUser(@PathVariable String businessId) {
        try {
            userInfoService.removeById(businessId);
        } catch (Exception e) {
            logger.error("Remove user occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
