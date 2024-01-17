/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * user controller.
 *
 * @author liwenqiang 2018/8/2 21:02
 **/
@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询信息
     *
     * @param id 主键
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserVO> fetch(@PathVariable Long id) {
        UserVO userVO;
        try {
            userVO = userService.fetch(id);
        } catch (Exception e) {
            logger.info("Fetch user occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userVO);
    }

    /**
     * 修改信息.
     *
     * @param id      主键
     * @param userDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserVO> modify(@PathVariable Long id,
                                         @RequestBody @Valid UserDTO userDTO) {
        UserVO userVO;
        try {
            userVO = userService.modify(id, userDTO);
        } catch (Exception e) {
            logger.error("Modify user occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(userVO);
    }

}
