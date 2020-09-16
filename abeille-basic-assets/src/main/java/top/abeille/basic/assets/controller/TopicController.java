/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.TopicDTO;
import top.abeille.basic.assets.service.TopicService;
import top.abeille.basic.assets.vo.TopicVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 话题信息controller
 *
 * @author liwenqiang 2020/2/16 14:26
 **/
@RestController
@RequestMapping("/topic")
public class TopicController extends AbstractController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * 分页查询信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<TopicVO> retrieveTopic() {
        return topicService.retrieveAll();
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<TopicVO> fetchTopic(@PathVariable String code) {
        return topicService.fetchByCode(code);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param topicDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<TopicVO> createTopic(@RequestBody @Valid TopicDTO topicDTO) {
        return topicService.create(topicDTO);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code     代码
     * @param topicDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    public Mono<TopicVO> modifyTopic(@PathVariable String code, @RequestBody @Valid TopicDTO topicDTO) {
        return topicService.modify(code, topicDTO);
    }

}
