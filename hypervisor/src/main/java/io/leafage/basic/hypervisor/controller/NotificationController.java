package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.NotificationDTO;
import io.leafage.basic.hypervisor.service.NotificationService;
import io.leafage.basic.hypervisor.vo.NotificationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * account controller.
 *
 * @author liwenqiang 2022/1/29 18:05
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Page<NotificationVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Page<NotificationVO> voPage;
        try {
            voPage = notificationService.retrieve(page, size);
        } catch (Exception e) {
            logger.info("Retrieve notification occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voPage);
    }

    /**
     * 根据 code 查询
     *
     * @param code 代码
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<NotificationVO> fetch(@PathVariable String code) {
        NotificationVO notificationVO;
        try {
            notificationVO = notificationService.fetch(code);
        } catch (Exception e) {
            logger.info("Fetch notification occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notificationVO);
    }

    /**
     * 新增信息
     *
     * @param notificationDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<NotificationVO> create(@RequestBody @Valid NotificationDTO notificationDTO) {
        NotificationVO vo;
        try {
            vo = notificationService.create(notificationDTO);
        } catch (Exception e) {
            logger.info("Create notification occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }
}
