package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.service.DictionaryService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * dictionary controller.
 *
 * @author liwenqiang 2022-04-06 17:44
 **/
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    private final Logger logger = LoggerFactory.getLogger(DictionaryController.class);

    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Page<DictionaryVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Page<DictionaryVO> voPage;
        try {
            voPage = dictionaryService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve dictionary occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voPage);
    }

    /**
     * 查询下级数据
     *
     * @return 查询到的数据，否则返回空
     */
    @GetMapping("/{code}/lower")
    public ResponseEntity<List<DictionaryVO>> lower(@PathVariable Long code) {
        List<DictionaryVO> child;
        try {
            child = dictionaryService.lower(code);
        } catch (Exception e) {
            logger.info("Retrieve dictionary lower occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(child);
    }

    /**
     * 根据 code 查询
     *
     * @param code 业务id
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<DictionaryVO> fetch(@PathVariable Long code) {
        DictionaryVO vo;
        try {
            vo = dictionaryService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch dictionary occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vo);
    }

    /**
     * 是否存在
     *
     * @param name 名称
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{name}/exist")
    public ResponseEntity<Boolean> exist(@PathVariable String name) {
        boolean exist;
        try {
            exist = dictionaryService.exist(name);
        } catch (Exception e) {
            logger.info("Query dictionary exist occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(exist);
    }

    /**
     * 添加信息
     *
     * @param dictionaryDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<DictionaryVO> create(@RequestBody DictionaryDTO dictionaryDTO) {
        DictionaryVO vo;
        try {
            vo = dictionaryService.create(dictionaryDTO);
        } catch (Exception e) {
            logger.error("Create dictionary occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

}
