/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.TopicInfo;
import top.abeille.basic.assets.dto.TopicDTO;
import top.abeille.basic.assets.repository.TopicInfoRepository;
import top.abeille.basic.assets.service.TopicInfoService;
import top.abeille.basic.assets.vo.TopicVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.Objects;

/**
 * 话题信息service实现
 *
 * @author liwenqiang 2020/2/13 20:24
 **/
@Service
public class TopicInfoServiceImpl extends AbstractBasicService implements TopicInfoService {

    private final TopicInfoRepository topicInfoRepository;

    public TopicInfoServiceImpl(TopicInfoRepository topicInfoRepository) {
        this.topicInfoRepository = topicInfoRepository;
    }

    @Override
    public Flux<TopicVO> retrieveAll(Sort sort) {
        return topicInfoRepository.findAll(sort).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<TopicVO> fetchById(String topicId) {
        Objects.requireNonNull(topicId);
        return fetchByTopicId(topicId).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<TopicVO> create(TopicDTO topicDTO) {
        TopicInfo info = new TopicInfo();
        BeanUtils.copyProperties(topicDTO, info);
        info.setTopicId(this.getDateValue());
        info.setEnabled(Boolean.TRUE);
        return topicInfoRepository.save(info).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<TopicVO> modify(String topicId, TopicDTO topicDTO) {
        Objects.requireNonNull(topicId);
        return fetchByTopicId(topicId).flatMap(topicInfo -> {
            BeanUtils.copyProperties(topicDTO, topicInfo);
            return topicInfoRepository.save(topicInfo).filter(Objects::nonNull).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String topicId) {
        Objects.requireNonNull(topicId);
        TopicInfo info = new TopicInfo();
        info.setTopicId(topicId);
        return topicInfoRepository.findOne(Example.of(info)).flatMap(topic -> topicInfoRepository.deleteById(topic.getId()));
    }

    /**
     * 根据ID查询
     *
     * @param topicId 文章ID
     * @return ArticleInfo 对象
     */
    private Mono<TopicInfo> fetchByTopicId(String topicId) {
        Objects.requireNonNull(topicId);
        TopicInfo info = new TopicInfo();
        info.setTopicId(topicId);
        return topicInfoRepository.findOne(Example.of(info));
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param info 信息
     * @return ArticleVO 输出对象
     */
    private TopicVO convertOuter(TopicInfo info) {
        TopicVO outer = new TopicVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
