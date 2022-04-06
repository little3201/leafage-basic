package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.entity.Dictionary;
import io.leafage.basic.hypervisor.repository.DictionaryRepository;
import io.leafage.basic.hypervisor.service.DictionaryService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * dictionary service impl.
 *
 * @author liwenqiang 2022-04-06 17:38
 **/
@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public Page<DictionaryVO> retrieve(int page, int size) {
        return dictionaryRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public List<DictionaryVO> lower(long code) {
        return dictionaryRepository.findBySuperiorAndEnabledTrue(code)
                .stream().map(this::convertOuter).collect(Collectors.toList());
    }

    @Override
    public boolean exist(String name) {
        return dictionaryRepository.existsByName(name);
    }

    @Override
    public DictionaryVO create(DictionaryDTO dictionaryDTO) {
        return DictionaryService.super.create(dictionaryDTO);
    }

    /**
     * 数据转换
     *
     * @param dictionary 信息
     * @return RegionVO 输出对象
     */
    private DictionaryVO convertOuter(Dictionary dictionary) {
        DictionaryVO vo = new DictionaryVO();
        BeanUtils.copyProperties(dictionary, vo);

        if (dictionary.getSuperior() != null) {
            Optional<Dictionary> optional = dictionaryRepository.findById(dictionary.getSuperior());
            optional.ifPresent(superior -> vo.setSuperior(superior.getName()));
        }
        return vo;
    }
}
