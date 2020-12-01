/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.CategoryDTO;
import top.abeille.basic.assets.service.CategoryService;
import top.abeille.basic.assets.vo.CategoryVO;

import javax.validation.Valid;

/**
 * 话题信息controller
 *
 * @author liwenqiang 2020/2/16 14:26
 **/
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 分页查询信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<CategoryVO> retrieveCategory() {
        return categoryService.retrieveAll();
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<CategoryVO> fetchCategory(@PathVariable String code) {
        return categoryService.fetchByCode(code);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param categoryDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CategoryVO> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.create(categoryDTO);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code        代码
     * @param categoryDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<CategoryVO> modifyCategory(@PathVariable String code, @RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.modify(code, categoryDTO);
    }

}
