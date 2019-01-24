package top.abeille.basic.authority.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.authority.model.UserInfoModel;
import top.abeille.basic.authority.service.IUserInfoService;
import top.abeille.basic.authority.view.UserView;
import top.abeille.basic.common.controller.BasicController;

/**
 * 用户信息Controller
 *
 * @author liwenqiang 2018/8/2 21:02
 **/
@Api(tags = "User Service Api")
@RestController
@RequestMapping("/user/v1")
public class UserInfoController extends BasicController {

    private final IUserInfoService userInfoService;

    @Autowired
    public UserInfoController(IUserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 用户查询——分页
     *
     * @return ResponseEntity
     */
    @ApiOperation(value = "Fetch enabled users with pageable")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity findUsers(Pageable pageable) {
        return ResponseEntity.ok(userInfoService.findAllByPage(pageable));
    }

    /**
     * 用户查询——根据ID
     *
     * @param id
     * @return ResponseEntity
     */
    @ApiOperation(value = "Get single user by id")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    @GetMapping("/option")
    @JsonView(UserView.Details.class)
    public ResponseEntity getOption(Long id) {
        return ResponseEntity.ok(userInfoService.getById(id));
    }

    /**
     * 保存用户
     *
     * @param user
     * @return ResponseEntity
     */
    @ApiOperation(value = "Save single user")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/option")
    public ResponseEntity saveOption(UserInfoModel user) {
        try {
            userInfoService.save(user);
        } catch (Exception e) {
            log.error("Save user occurred an error={}", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }

    /**
     * 编辑用户
     *
     * @param user
     * @return ResponseEntity
     */
    @ApiOperation(value = "Modify single user")
    @PutMapping("/option")
    public ResponseEntity modifyOption(UserInfoModel user) {
        if (user.getId() == null) {
            return ResponseEntity.ok("Request param is null");
        }
        try {
            userInfoService.update(user);
        } catch (Exception e) {
            log.error("Modify user occurred an error={}", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }

    /**
     * 删除用户——根据ID
     *
     * @param id
     * @return ResponseEntity
     */
    @ApiOperation(value = "Remove single user")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/option")
    public ResponseEntity removeOption(Long id) {
        try {
            userInfoService.removeById(id);
        } catch (Exception e) {
            log.error("Remove user occurred an error={}", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }
}
