package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Dictionary;
import io.leafage.basic.hypervisor.repository.DictionaryRepository;
import io.leafage.basic.hypervisor.service.DictionaryService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * dictionary service impl
 *
 * @author liwenqiang 2022-03-30 07:34
 **/
@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public Flux<DictionaryVO> lower(String code) {
        return dictionaryRepository.findBySuperiorAndEnabledTrue(code).map(this::convert);
    }

    @Override
    public Flux<DictionaryVO> retrieve(int page, int size) {
        return dictionaryRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convert);
    }

    @Override
    public Mono<Boolean> exist(String name) {
        return dictionaryRepository.existsByName(name);
    }

    @Override
    public Mono<Long> count() {
        return dictionaryRepository.count();
    }

    private DictionaryVO convert(Dictionary dictionary) {
        DictionaryVO vo = new DictionaryVO();
        BeanUtils.copyProperties(dictionary, vo);
        return vo;
    }
}
