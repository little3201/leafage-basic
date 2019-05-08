package top.abeille.basic.profile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.profile.model.GroupInfoModel;
import top.abeille.basic.profile.service.GroupInfoService;
import top.abeille.common.basic.BasicController;

/**
 * 组织信息controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
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
    @GetMapping("/v1/group")
    public ResponseEntity getGroup(Long id) {
        if (id == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        GroupInfoModel groupInfo = groupInfoService.getById(id);
        if (groupInfo == null) {
            log.info("Not found anything about group with id {}.", id);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(groupInfo);
    }

    /**
     * 查找组织信息——分页查询
     *
     * @param curPage  查询页码
     * @param pageSize 分页大小
     * @return ResponseEntity
     */
    @GetMapping("/v1/groups")
    public ResponseEntity findGroups(Integer curPage, Integer pageSize) {
        if (curPage == null || pageSize == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        Page<GroupInfoModel> groups = groupInfoService.findAllByPage(curPage, pageSize);
        if (CollectionUtils.isEmpty(groups.getContent())) {
            log.info("Not found anything about group with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 保存组织信息
     *
     * @param group 组织
     * @return ResponseEntity
     */
    @PostMapping("/v1/group")
    public ResponseEntity saveGroup(@RequestBody GroupInfoModel group) {
        try {
            groupInfoService.save(group);
        } catch (Exception e) {
            log.error("Save group occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 修改组织信息
     *
     * @param group 组织
     * @return ResponseEntity
     */
    @PutMapping("/v1/group")
    public ResponseEntity modifyGroup(@RequestBody GroupInfoModel group) {
        try {
            groupInfoService.save(group);
        } catch (Exception e) {
            log.error("Modify group occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除组织信息
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @DeleteMapping("/v1/group")
    public ResponseEntity removeGroup(Long id) {
        try {
            groupInfoService.removeById(id);
        } catch (Exception e) {
            log.error("Remove group occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.MOVED_PERMANENTLY);
    }
}
