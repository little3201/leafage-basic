package top.abeille.basic.authority.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.authority.model.RoleInfoModel;
import top.abeille.basic.authority.service.IRoleInfoService;
import top.abeille.basic.common.controller.BasicController;

/**
 * 角色信息controller
 *
 * @author liwenqiang 2018/12/17 19:38
 **/
@Api(tags = "Role Service Api")
@RequestMapping("/role/v1")
public class RoleInfoController extends BasicController {

    private final IRoleInfoService roleInfoService;

    @Autowired
    public RoleInfoController(IRoleInfoService roleInfoService) {
        this.roleInfoService = roleInfoService;
    }

    /**
     * 角色查询——分页
     *
     * @param pageable param of page
     * @return ResponseEntity
     */
    @ApiOperation(value = "Fetch enabled roles with pageable")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/roles")
    public ResponseEntity findRoles(Pageable pageable) {
        return ResponseEntity.ok(roleInfoService.findAllByPage(pageable));
    }

    /**
     * 角色查询——根据ID
     *
     * @param id
     * @return ResponseEntity
     */
    @ApiOperation(value = "Get single role by id")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/option")
    public ResponseEntity getOption(Long id) {
        return ResponseEntity.ok(roleInfoService.getById(id));
    }

    /**
     * 保存角色
     *
     * @param role
     * @return ResponseEntity
     */
    @ApiOperation(value = "Save single role")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/option")
    public ResponseEntity saveOption(RoleInfoModel role) {
        try {
            roleInfoService.save(role);
        } catch (Exception e) {
            log.error("Save role occurred an error={}", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }

    /**
     * 编辑角色
     *
     * @param role
     * @return ResponseEntity
     */
    @ApiOperation(value = "Modify single role")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PutMapping("/option")
    public ResponseEntity modifyOption(RoleInfoModel role) {
        try {
            roleInfoService.update(role);
        } catch (Exception e) {
            log.error("Modify role occurred an error={}", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }

    /**
     * 删除角色——根据ID
     *
     * @param id
     * @return ResponseEntity
     */
    @ApiOperation(value = "Remove single role")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/option")
    public ResponseEntity removeOption(Long id) {
        try {
            roleInfoService.removeById(id);
        } catch (Exception e) {
            log.error("Remove role occurred an error={}", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }

}
