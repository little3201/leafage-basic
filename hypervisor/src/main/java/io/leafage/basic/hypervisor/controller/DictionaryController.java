package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.service.DictionaryService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * dictionary controller
 *
 * @author liwenqiang 2022/04/02 17:19
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
    public ResponseEntity<Flux<DictionaryVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Flux<DictionaryVO> voFlux;
        try {
            voFlux = dictionaryService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve dictionary occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 根据 code 查询
     *
     * @param code 业务id
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<Mono<DictionaryVO>> fetch(@PathVariable String code) {
        Mono<DictionaryVO> voMono;
        try {
            voMono = dictionaryService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch dictionary occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 统计记录数
     *
     * @return 查询的记录数，异常时返回204状态码
     */
    @GetMapping("/count")
    public ResponseEntity<Mono<Long>> count() {
        Mono<Long> count;
        try {
            count = dictionaryService.count();
        } catch (Exception e) {
            logger.error("Count dictionary occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 查询下级数据
     *
     * @return 查询到的数据，否则返回空
     */
    @GetMapping("/{code}/lower")
    public ResponseEntity<Flux<DictionaryVO>> lower(@PathVariable String code) {
        Flux<DictionaryVO> voFlux;
        try {
            voFlux = dictionaryService.lower(code);
        } catch (Exception e) {
            logger.info("Retrieve dictionary lower occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 添加信息
     *
     * @param dictionaryDTO 要添加的数据
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<DictionaryVO>> create(@RequestBody DictionaryDTO dictionaryDTO) {
        Mono<DictionaryVO> voMono;
        try {
            voMono = dictionaryService.create(dictionaryDTO);
        } catch (Exception e) {
            logger.info("Create dictionary occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }
}
