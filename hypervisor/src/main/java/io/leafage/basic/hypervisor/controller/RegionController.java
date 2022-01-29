package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * region controller.
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
     * 查询下级数据
     *
     * @return 查询到的数据，否则返回空
     */
    @GetMapping("/{code}/lower")
    public ResponseEntity<List<RegionVO>> child(@PathVariable Long code) {
        List<RegionVO> child;
        try {
            child = regionService.child(code);
        } catch (Exception e) {
            logger.info("Retrieve region tree occurred an error: ", e);
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
            exist = regionService.exist(name);
        } catch (Exception e) {
            logger.info("Query region exist occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(exist);
    }

    /**
     * 添加信息
     *
     * @param regionDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<RegionVO> create(@RequestBody RegionDTO regionDTO) {
        RegionVO regionVO;
        try {
            regionVO = regionService.create(regionDTO);
        } catch (Exception e) {
            logger.error("Create region occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(regionVO);
    }

    /**
     * 修改信息
     *
     * @param code      代码
     * @param regionDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<RegionVO> modify(@PathVariable Long code, @RequestBody RegionDTO regionDTO) {
        RegionVO regionVO;
        try {
            regionVO = regionService.modify(code, regionDTO);
        } catch (Exception e) {
            logger.error("Modify region occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(regionVO);
    }

    /**
     * 删除信息
     *
     * @param code 代码
     * @return 如果删除成功，返回200状态码，否则返回417状态码
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> remove(@PathVariable Long code) {
        try {
            regionService.remove(code);
        } catch (Exception e) {
            logger.error("Remove region occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }
}
