package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Dictionary;
import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.repository.DictionaryRepository;
import io.leafage.basic.hypervisor.service.DictionaryService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.ValidMessage;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;

/**
 * dictionary service impl
 *
 * @author liwenqiang 2022-03-30 07:34
 **/
@Service
public class DictionaryServiceImpl extends ReactiveAbstractTreeNodeService<Dictionary> implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public Mono<Page<DictionaryVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<DictionaryVO> voFlux = dictionaryRepository.findByEnabledTrue(pageRequest).map(this::convert);

        Mono<Long> count = dictionaryRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).flatMap(objects ->
                Mono.just(new PageImpl<>(objects.getT1(), pageRequest, objects.getT2())));
    }

    @Override
    public Flux<DictionaryVO> superior() {
        return dictionaryRepository.findBySuperiorIsNullAndEnabledTrue().map(this::convert);
    }

    @Override
    public Flux<DictionaryVO> lower(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return dictionaryRepository.findBySuperiorAndEnabledTrue(code).map(this::convert);
    }

    @Override
    public Mono<DictionaryVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return dictionaryRepository.getByCodeAndEnabledTrue(code).map(this::convert);
    }

    @Override
    public Mono<Boolean> exist(String name) {
        Assert.hasText(name, ValidMessage.NAME_NOT_BLANK);
        return dictionaryRepository.existsByName(name);
    }

    @Override
    public Mono<DictionaryVO> create(DictionaryDTO dictionaryDTO) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryDTO, dictionary);
        dictionary.setCode(this.generateCode());
        return dictionaryRepository.insert(dictionary).map(this::convert);
    }

    private DictionaryVO convert(Dictionary dictionary) {
        DictionaryVO vo = new DictionaryVO();
        BeanUtils.copyProperties(dictionary, vo);
        return vo;
    }
}
