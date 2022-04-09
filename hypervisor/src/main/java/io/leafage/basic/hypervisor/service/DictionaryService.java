package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import reactor.core.publisher.Flux;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * dictionary service
 *
 * @author liwenqiang 2022-03-30 07:34
 **/
public interface DictionaryService extends ReactiveBasicService<DictionaryDTO, DictionaryVO, String> {

    /**
     * 获取下级
     *
     * @return 数据集
     */
    Flux<DictionaryVO> lower(String code);
}
