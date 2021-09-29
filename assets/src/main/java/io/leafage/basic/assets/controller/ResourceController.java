package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.service.ResourceService;
import io.leafage.basic.assets.vo.ResourceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * resource api .
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/resource")
public class ResourceController {

    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询到数据集，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Page<ResourceVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Page<ResourceVO> voFlux;
        try {
            voFlux = resourceService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve portfolio occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 查询信息
     *
     * @param code 代码
     * @return 查询到数据，异常时返回204
     */
    @GetMapping("/{code}")
    public ResponseEntity<ResourceVO> fetch(@PathVariable String code) {
        ResourceVO vo;
        try {
            vo = resourceService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch portfolio occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vo);
    }

    /**
     * 是否已存在
     *
     * @param title 标题
     * @return true-是，false-否
     */
    @GetMapping("/exist")
    public ResponseEntity<Boolean> exist(@RequestParam String title) {
        boolean exists;
        try {
            exists = resourceService.exist(title);
        } catch (Exception e) {
            logger.error("Check portfolio is exist an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(exists);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param resourceDTO 要添加的数据
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<ResourceVO> create(@RequestBody @Valid ResourceDTO resourceDTO) {
        ResourceVO vo;
        try {
            vo = resourceService.create(resourceDTO);
        } catch (Exception e) {
            logger.error("Create portfolio occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code        代码
     * @param resourceDTO 要修改的数据
     * @return 修改后的信息，异常时返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<ResourceVO> modify(@PathVariable String code, @RequestBody @Valid ResourceDTO resourceDTO) {
        ResourceVO vo;
        try {
            vo = resourceService.modify(code, resourceDTO);
        } catch (Exception e) {
            logger.error("Modify portfolio occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(vo);
    }

}
