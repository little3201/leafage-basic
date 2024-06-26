/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Dictionary;
import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.repository.DictionaryRepository;
import io.leafage.basic.hypervisor.service.DictionaryService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.leafage.common.servlet.ServletAbstractTreeNodeService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * dictionary service impl.
 *
 * @author wq li 2022-04-06 17:38
 **/
@Service
public class DictionaryServiceImpl extends ServletAbstractTreeNodeService<Dictionary> implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public Page<DictionaryVO> retrieve(int page, int size) {
        return dictionaryRepository.findBySuperiorIdIsNull(PageRequest.of(page, size)).map(this::convert);
    }

    @Override
    public DictionaryVO fetch(Long id) {
        Assert.notNull(id, "dictionary id must not be null.");
        Dictionary dictionary = dictionaryRepository.findById(id).orElse(null);
        if (dictionary == null) {
            return null;
        }
        return this.convert(dictionary);
    }

    @Override
    public List<DictionaryVO> subset(Long id) {
        Assert.notNull(id, "dictionary id must not be null.");
        return dictionaryRepository.findBySuperiorId(id)
                .stream().map(this::convert).toList();
    }

    @Override
    public boolean exist(String name) {
        Assert.hasText(name, "dictionary name must not be blank.");
        return dictionaryRepository.existsByName(name);
    }

    @Override
    public DictionaryVO create(DictionaryDTO dto) {
        Dictionary dictionary = new Dictionary();
        BeanCopier copier = BeanCopier.create(DictionaryDTO.class, Dictionary.class, false);
        copier.copy(dto, dictionary, null);

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
        BeanCopier copier = BeanCopier.create(Dictionary.class, DictionaryVO.class, false);
        copier.copy(dictionary, vo, null);

        // get lastModifiedDate
        Optional<Instant> optionalInstant = dictionary.getLastModifiedDate();
        optionalInstant.ifPresent(vo::setLastModifiedDate);
        return vo;
    }
}
