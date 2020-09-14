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
import top.abeille.basic.hypervisor.service.UserService;
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

    private final UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询翻译信息
     *
     * @param pageNum  当前页
     * @param pageSize 页内数据量
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回204
     */
    @GetMapping
    public ResponseEntity<Object> retrieveUser(Integer pageNum, Integer pageSize) {
        Pageable pageable = super.initPageParams(pageNum, pageSize);
        Page<UserVO> users = userService.retrieveByPage(pageable);
        if (CollectionUtils.isEmpty(users.getContent())) {
            logger.info("Not found anything about user with pageable.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    /**
     * 根据传入的业务id: businessId 查询信息
     *
     * @param businessId 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{businessId}")
    public ResponseEntity<Object> fetchUser(@PathVariable String businessId) {
        UserVO userVO = userService.fetchByBusinessId(businessId);
        if (Objects.isNull(userVO)) {
            logger.info("Not found anything about hypervisor with userId: {}.", businessId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userVO);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param userDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            userService.create(userDTO);
        } catch (Exception e) {
            logger.error("Save user occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId 业务id
     * @param userDTO    要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{businessId}")
    public ResponseEntity<Object> modifyUser(@PathVariable String businessId, @RequestBody @Valid UserDTO userDTO) {
        try {
            userService.modify(businessId, userDTO);
        } catch (Exception e) {
            logger.error("Modify user occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 根据传入的业务id: businessId 删除信息
     *
     * @param businessId 业务id
     * @return 如果删除成功，返回200状态码，否则返回417状态码
     */
    @DeleteMapping("/{businessId}")
    public ResponseEntity<Object> removeUser(@PathVariable String businessId) {
        try {
            userService.removeById(businessId);
        } catch (Exception e) {
            logger.error("Remove user occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
