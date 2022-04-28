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
import org.springframework.util.Assert;
import top.leafage.common.basic.ValidMessage;
import top.leafage.common.servlet.ServletAbstractTreeNodeService;
import java.util.List;

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
        return dictionaryRepository.findAll(PageRequest.of(page, size)).map(this::convert);
    }

    @Override
    public DictionaryVO fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Dictionary dictionary = dictionaryRepository.getByCodeAndEnabledTrue(code);
        return this.convert(dictionary);
    }

    @Override
    public List<DictionaryVO> lower(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return dictionaryRepository.findBySuperiorAndEnabledTrue(code)
                .stream().map(this::convert).toList();
    }

    @Override
    public boolean exist(String name) {
        Assert.hasText(name, ValidMessage.NAME_NOT_BLANK);
        return dictionaryRepository.existsByName(name);
    }

    @Override
    public DictionaryVO create(DictionaryDTO dictionaryDTO) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryDTO, dictionary);
        dictionary.setCode(this.generateCode());
        dictionaryRepository.saveAndFlush(dictionary);

        return this.convert(dictionary);
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
}
