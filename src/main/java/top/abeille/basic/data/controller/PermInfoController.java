package top.abeille.basic.data.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.common.controller.BasicController;
import top.abeille.basic.data.model.PermInfoModel;
import top.abeille.basic.data.service.IPermInfoService;

/**
 * 权限资源controller
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
@Api(tags = "Permission Service Api")
@RequestMapping("/permission/v1")
@RestController
public class PermInfoController extends BasicController {

    private final IPermInfoService permInfoService;

    @Autowired
    public PermInfoController(IPermInfoService permInfoService) {
        this.permInfoService = permInfoService;
    }

    /**
     * 权限查询——分页
     *
     * @param pageable
     * @return ResponseEntity
     */
    @ApiOperation(value = "Fetch enabled permissions with pageable")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/permissions")
    public ResponseEntity findPermissions(@RequestBody Pageable pageable) {
        return ResponseEntity.ok(permInfoService.findAllByPage(pageable));
    }

    /**
     * 权限查询——根据ID
     *
     * @param id
     * @return ResponseEntity
     */
    @ApiOperation(value = "Get single permission by id")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/option")
    public ResponseEntity getOption(Long id) {
        if (id == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        PermInfoModel permission = permInfoService.getById(id);
        if (permission == null) {
            log.info("Not found anything of permission with id {}." + id);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(permission);
    }

    /**
     * 保存权限
     *
     * @param permission
     * @return ResponseEntity
     */
    @ApiOperation(value = "Save single permission")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/option")
    public ResponseEntity saveOption(@RequestBody PermInfoModel permission) {
        try {
            permInfoService.save(permission);
        } catch (Exception e) {
            log.error("Save permission occurred an error", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }

    /**
     * 编辑权限
     *
     * @param permission
     * @return ResponseEntity
     */
    @ApiOperation(value = "Modify single permission")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PutMapping("/option")
    public ResponseEntity modifyOption(@RequestBody PermInfoModel permission) {
        try {
            permInfoService.update(permission);
        } catch (Exception e) {
            log.error("Modify permission occurred an error", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }

    /**
     * 删除权限——根据ID
     *
     * @param id
     * @return ResponseEntity
     */
    @ApiOperation(value = "Remove single permission")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/option")
    public ResponseEntity removeOption(Long id) {
        try {
            permInfoService.removeById(id);
        } catch (Exception e) {
            log.error("Remove permission occurred an error={}", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }

}
