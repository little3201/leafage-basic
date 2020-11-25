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
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.service.GroupService;
import top.abeille.basic.hypervisor.vo.GroupVO;
import top.abeille.common.basic.AbstractController;

/**
 * 组信息接口
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/group")
public class GroupInfoController extends AbstractController {

    private final GroupService groupService;

    public GroupInfoController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * 分页查询翻译信息
     *
     * @param pageNum  当前页
     * @param pageSize 页内数据量
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public ResponseEntity<Object> retrieveGroup(Integer pageNum, Integer pageSize) {
        Pageable pageable = super.initPageParams(pageNum, pageSize);
        Page<GroupVO> groups = groupService.retrieveByPage(pageable);
        if (CollectionUtils.isEmpty(groups.getContent())) {
            logger.info("Not found anything about group with pageable.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 根据传入的业务id: businessId 查询信息
     *
     * @param businessId 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{businessId}")
    public ResponseEntity<Object> fetchGroup(@PathVariable String businessId) {
        GroupVO groupInfo = groupService.fetchByBusinessId(businessId);
        if (groupInfo == null) {
            logger.info("Not found anything about group with id {}.", businessId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(groupInfo);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param groupDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<Object> createGroup(@RequestBody GroupDTO groupDTO) {
        try {
            groupService.create(groupDTO);
        } catch (Exception e) {
            logger.error("Save group occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId 业务id
     * @param groupDTO   要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{businessId}")
    public ResponseEntity<Object> modifyGroup(@PathVariable String businessId, @RequestBody GroupDTO groupDTO) {
        try {
            groupService.modify(businessId, groupDTO);
        } catch (Exception e) {
            logger.error("Modify group occurred an error: ", e);
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
    public ResponseEntity<Object> removeGroup(@PathVariable String businessId) {
        try {
            groupService.removeById(businessId);
        } catch (Exception e) {
            logger.error("Remove group occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
