package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.service.CommentService;
import io.leafage.basic.assets.vo.CommentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * comment api .
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/comment")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 列表查询
     *
     * @param page 分页位置
     * @param size 分页大小
     * @return 查询到数据集，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Page<CommentVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Page<CommentVO> voPage;
        try {
            voPage = commentService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve comment occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voPage);
    }

    /**
     * 根据posts查询
     *
     * @param code 帖子代码
     * @return 关联的评论
     */
    @GetMapping("/{code}")
    public ResponseEntity<List<CommentVO>> posts(@PathVariable String code) {
        List<CommentVO> voList;
        try {
            voList = commentService.posts(code);
        } catch (Exception e) {
            logger.error("Fetch comment by posts occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voList);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param commentDTO 要添加的数据
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<CommentVO> create(@RequestBody @Valid CommentDTO commentDTO) {
        CommentVO vo;
        try {
            vo = commentService.create(commentDTO);
        } catch (Exception e) {
            logger.error("Create comment occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

}
