package top.abeille.basic.profile.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.profile.data.service.GroupInfoService;
import top.abeille.basic.profile.data.model.GroupInfoModel;
import top.abeille.common.basic.BasicController;

/**
 * 组织信息controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RequestMapping("/group/v1")
@RestController
public class GroupInfoController extends BasicController {

    private final GroupInfoService groupInfoService;

    @Autowired
    public GroupInfoController(GroupInfoService groupInfoService) {
        this.groupInfoService = groupInfoService;
    }

    /**
     * 查找组织信息——根据ID
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @GetMapping("/option")
    public ResponseEntity getOption(Long id) {
        if (id == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        GroupInfoModel groupInfo = groupInfoService.getById(id);
        if (groupInfo == null) {
            log.info("Not found anything about group with id {}." + id);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(groupInfo);
    }

    /**
     * 保存组织信息
     *
     * @param group 组织
     * @return ResponseEntity
     */
    @PostMapping("/option")
    public ResponseEntity saveOption(@RequestBody GroupInfoModel group) {
        try {
            groupInfoService.save(group);
        } catch (Exception e) {
            log.error("Save group occurred an error: {}", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }

    /**
     * 修改组织信息
     *
     * @param group 组织
     * @return ResponseEntity
     */
    @PutMapping("/option")
    public ResponseEntity modifyOption(@RequestBody GroupInfoModel group) {
        try {
            groupInfoService.save(group);
        } catch (Exception e) {
            log.error("Modify group occurred an error: {}", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }

    /**
     * 删除组织信息
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @DeleteMapping("/option")
    public ResponseEntity removeOption(Long id) {
        try {
            groupInfoService.removeById(id);
        } catch (Exception e) {
            log.error("Remove group occurred an error: {}", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok("success");
    }
}
