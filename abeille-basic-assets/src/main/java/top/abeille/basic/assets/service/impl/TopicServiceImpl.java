/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.constant.PrefixEnum;
import top.abeille.basic.assets.document.TopicInfo;
import top.abeille.basic.assets.dto.TopicDTO;
import top.abeille.basic.assets.repository.TopicRepository;
import top.abeille.basic.assets.service.TopicService;
import top.abeille.basic.assets.vo.TopicVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;

/**
 * 话题信息service实现
 *
 * @author liwenqiang 2020/2/13 20:24
 **/
@Service
public class TopicServiceImpl extends AbstractBasicService implements TopicService {

    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public Flux<TopicVO> retrieveAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return topicRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<TopicVO> fetchByBusinessId(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        TopicInfo info = new TopicInfo();
        info.setBusinessId(businessId);
        return this.fetchInfo(businessId).map(this::convertOuter);
    }

    @Override
    public Mono<TopicVO> create(TopicDTO topicDTO) {
        TopicInfo info = new TopicInfo();
        BeanUtils.copyProperties(topicDTO, info);
        info.setBusinessId(PrefixEnum.TP + this.generateId());
        info.setEnabled(Boolean.TRUE);
        info.setModifyTime(LocalDateTime.now());
        return topicRepository.insert(info).map(this::convertOuter);
    }

    @Override
    public Mono<TopicVO> modify(String businessId, TopicDTO topicDTO) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchInfo(businessId).flatMap(topicInfo -> {
            BeanUtils.copyProperties(topicDTO, topicInfo);
            topicInfo.setModifyTime(LocalDateTime.now());
            return topicRepository.save(topicInfo).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchInfo(businessId).flatMap(topic -> topicRepository.deleteById(topic.getId()));
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    private Mono<TopicInfo> fetchInfo(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        TopicInfo info = new TopicInfo();
        info.setBusinessId(businessId);
        return topicRepository.findOne(Example.of(info));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private TopicVO convertOuter(TopicInfo info) {
        TopicVO outer = new TopicVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
