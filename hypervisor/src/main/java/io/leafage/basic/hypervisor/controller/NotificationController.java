package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.service.NotificationService;
import io.leafage.basic.hypervisor.vo.NotificationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回204
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
     * 查询信息
     *
     * @param username 账户
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{username}")
    public ResponseEntity<NotificationVO> fetch(@PathVariable String username) {
        NotificationVO notificationVO;
        try {
            notificationVO = notificationService.fetch(username);
        } catch (Exception e) {
            logger.info("Fetch notification occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notificationVO);
    }
}
