package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.service.AccessLogService;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * access log controller.
 *
 * @author wq li 2022/4/15 13:50
 **/
@RestController
@RequestMapping("/access-logs")
public class AccessLogController {

    private final Logger logger = LoggerFactory.getLogger(AccessLogController.class);

    private final AccessLogService accessLogService;

    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    /**
     * 查询
     *
     * @return 查询到数据集，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Page<AccessLogVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Page<AccessLogVO> voPage;
        try {
            voPage = accessLogService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve record occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voPage);
    }

    /**
     * 新增信息
     *
     * @param accessLogDTO 帖子代码
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<AccessLogVO> create(@RequestBody AccessLogDTO accessLogDTO) {
        AccessLogVO vo;
        try {
            vo = accessLogService.create(accessLogDTO);
        } catch (Exception e) {
            logger.error("Create record occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }
}
