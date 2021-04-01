/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 用户信息接口.
 *
 * @author liwenqiang 2018/8/2 21:02
 * @version 0.0.1
 * @since 1.0
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回204
     */
    @GetMapping
    public ResponseEntity<Object> retrieve(@RequestParam int page, @RequestParam int size, String order) {
        Page<UserVO> users = userService.retrieve(page, size, order);
        if (CollectionUtils.isEmpty(users.getContent())) {
            logger.info("Not found anything about user with pageable.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    /**
     * 查询信息
     *
     * @param username 账户
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{username}")
    public ResponseEntity<Object> fetch(@PathVariable String username) {
        UserVO userVO = userService.fetch(username);
        if (Objects.isNull(userVO)) {
            logger.info("Not found anything with username: {}.", username);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userVO);
    }

    /**
     * 查询信息
     *
     * @param username 账户
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/details/{username}")
    public ResponseEntity<Object> fetchDetails(@PathVariable String username) {
        UserDetails userDetails = userService.fetchDetails(username);
        if (Objects.isNull(userDetails)) {
            logger.info("Not found anything with username: {}.", username);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userDetails);
    }

    /**
     * 添加信息
     *
     * @param userDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDTO userDTO) {
        try {
            userService.create(userDTO);
        } catch (Exception e) {
            logger.error("Save user occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 修改信息.
     *
     * @param username 账户
     * @param userDTO  要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{username}")
    public ResponseEntity<Object> modify(@PathVariable String username,
                                         @RequestBody @Valid UserDTO userDTO) {
        try {
            userService.modify(username, userDTO);
        } catch (Exception e) {
            logger.error("Modify user occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除信息
     *
     * @param username 账户
     * @return 如果删除成功，返回200状态码，否则返回417状态码
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Object> remove(@PathVariable String username) {
        try {
            userService.remove(username);
        } catch (Exception e) {
            logger.error("Remove user occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
