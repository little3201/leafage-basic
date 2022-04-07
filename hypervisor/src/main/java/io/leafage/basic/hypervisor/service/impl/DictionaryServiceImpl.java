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
import top.leafage.common.servlet.ServletAbstractTreeNodeService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * dictionary service impl.
 *
 * @author liwenqiang 2022-04-06 17:38
 **/
@Service
public class DictionaryServiceImpl extends ServletAbstractTreeNodeService<Dictionary> implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public Page<DictionaryVO> retrieve(int page, int size) {
        return dictionaryRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public DictionaryVO fetch(String code) {
        Dictionary dictionary = dictionaryRepository.getByCodeAndEnabledTrue(code);
        return this.convert(dictionary);
    }

    @Override
    public List<DictionaryVO> lower(String code) {
        return dictionaryRepository.findBySuperiorAndEnabledTrue(code)
                .stream().map(this::convertOuter).collect(Collectors.toList());
    }

    @Override
    public boolean exist(String name) {
        return dictionaryRepository.existsByName(name);
    }

    @Override
    public DictionaryVO create(DictionaryDTO dictionaryDTO) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryDTO, dictionary);
        dictionary.setCode(this.generateCode());
        dictionaryRepository.save(dictionary);

        return this.convertOuter(dictionary);
    }

    /**
     * 类型转换
     *
     * @param dictionary 信息
     * @return DictionaryVO 输出对象
     */
    private DictionaryVO convert(Dictionary dictionary) {
        DictionaryVO vo = new DictionaryVO();
        BeanUtils.copyProperties(dictionary, vo);
        return vo;
    }

    /**
     * 数据转换
     *
     * @param dictionary 信息
     * @return DictionaryVO 输出对象
     */
    private DictionaryVO convertOuter(Dictionary dictionary) {
        DictionaryVO vo = this.convert(dictionary);

        if (dictionary.getSuperior() != null) {
            Dictionary superior = dictionaryRepository.getByCodeAndEnabledTrue(dictionary.getSuperior());
            vo.setSuperior(superior.getName());
        }
        return vo;
    }
}
