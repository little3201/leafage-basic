package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * dictionary service
 *
 * @author liwenqiang 2022-03-30 07:34
 **/
public interface DictionaryService extends ReactiveBasicService<DictionaryDTO, DictionaryVO, String> {


    /**
     * 获取上级
     *
     * @return 数据集
     */
    Flux<DictionaryVO> superior();

    /**
     * 获取下级
     *
     * @return 数据集
     */
    Flux<DictionaryVO> lower(String code);

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 结果集
     */
    Mono<Page<DictionaryVO>> retrieve(int page, int size);
}
