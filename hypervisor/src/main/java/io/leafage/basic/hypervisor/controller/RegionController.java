package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * region controller
 *
 * @author liwenqiang 2021/08/20 17:08
 **/
@RestController
@RequestMapping("/region")
public class RegionController {

    private final Logger logger = LoggerFactory.getLogger(RegionController.class);

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Page<RegionVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Page<RegionVO> voPage;
        try {
            voPage = regionService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve region occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voPage);
    }

    /**
     * 根据 code 查询
     *
     * @param code 业务id
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<RegionVO> fetch(@PathVariable Long code) {
        RegionVO vo;
        try {
            vo = regionService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch region occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vo);
    }

}
