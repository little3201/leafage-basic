/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.service.GroupService;
import top.abeille.basic.hypervisor.vo.CountVO;
import top.abeille.basic.hypervisor.vo.GroupVO;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * 组信息controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * 查询组信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<GroupVO> retrieve(@RequestParam int page, @RequestParam int size) {
        return groupService.retrieve(page, size);
    }

    /**
     * 根据传入的业务id: code 查询信息
     *
     * @param code 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<GroupVO> fetch(@PathVariable String code) {
        return groupService.fetch(code);
    }

    /**
     * 统计关联信息
     *
     * @param ids ID集合
     * @return 统计信息
     */
    @Validated
    @GetMapping("/count")
    public Flux<CountVO> countRelations(@NotEmpty Set<String> ids) {
        return groupService.countRelations(ids);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param groupDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GroupVO> create(@RequestBody @Valid GroupDTO groupDTO) {
        return groupService.create(groupDTO);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code     代码
     * @param groupDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<GroupVO> modify(@PathVariable String code, @RequestBody @Valid GroupDTO groupDTO) {
        return groupService.modify(code, groupDTO);
    }

    /**
     * 根据传入的代码删除信息（逻辑删除）
     *
     * @param code 代码
     * @return 如果删除数据成功，返回删除后的信息，否则返回417状态码
     */
    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> remove(@PathVariable String code) {
        return groupService.remove(code);
    }

}