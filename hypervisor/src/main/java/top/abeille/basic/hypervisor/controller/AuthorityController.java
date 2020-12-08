/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.AuthorityDTO;
import top.abeille.basic.hypervisor.service.AuthorityService;
import top.abeille.basic.hypervisor.vo.AuthorityVO;

import javax.validation.Valid;

/**
 * 权限 controller
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
@RestController
@RequestMapping("/authority")
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * 查询资源信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<AuthorityVO> retrieveAuthority(@RequestParam int page, @RequestParam int size) {
        return authorityService.retrieveAll(page, size);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param authorityDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthorityVO> createAuthority(@RequestBody @Valid AuthorityDTO authorityDTO) {
        return authorityService.create(authorityDTO);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code         代码
     * @param authorityDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<AuthorityVO> modifyAuthority(@PathVariable String code, @RequestBody @Valid AuthorityDTO authorityDTO) {
        return authorityService.modify(code, authorityDTO);
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<AuthorityVO> fetchAuthority(@PathVariable String code) {
        return authorityService.fetchByCode(code);
    }
}
