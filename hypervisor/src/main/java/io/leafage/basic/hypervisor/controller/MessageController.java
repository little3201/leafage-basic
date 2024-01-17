package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.MessageDTO;
import io.leafage.basic.hypervisor.service.MessageService;
import io.leafage.basic.hypervisor.vo.MessageVO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * account controller.
 *
 * @author liwenqiang 2022/1/29 18:05
 */
@RestController
@RequestMapping("/messages")
public class MessageController {

    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Page<MessageVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Page<MessageVO> voPage;
        try {
            voPage = messageService.retrieve(page, size);
        } catch (Exception e) {
            logger.info("Retrieve notification occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voPage);
    }

    /**
     * 根据 id 查询
     *
     * @param id 主键
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping("/{id}")
    public ResponseEntity<MessageVO> fetch(@PathVariable Long id) {
        MessageVO messageVO;
        try {
            messageVO = messageService.fetch(id);
        } catch (Exception e) {
            logger.info("Fetch notification occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messageVO);
    }

    /**
     * 新增信息
     *
     * @param messageDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<MessageVO> create(@RequestBody @Valid MessageDTO messageDTO) {
        MessageVO vo;
        try {
            vo = messageService.create(messageDTO);
        } catch (Exception e) {
            logger.info("Create notification occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }
}
