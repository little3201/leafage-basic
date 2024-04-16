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
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.Tag;
import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.repository.TagRepository;
import io.leafage.basic.assets.service.TagService;
import io.leafage.basic.assets.vo.CategoryVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Optional;

/**
 * tag service impl.
 *
 * @author wq li  2020-12-03 22:59
 **/
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public TagServiceImpl(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Page<CategoryVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "lastModifiedDate"));
        return tagRepository.findAll(pageable).map(tag -> {
            CategoryVO categoryVO = this.convertOuter(tag);
            long count = postRepository.countByCategoryId(tag.getId());
            categoryVO.setCount(count);
            return categoryVO;
        });
    }

    @Override
    public CategoryVO fetch(Long id) {
        Assert.notNull(id, "tag id must not be null.");
        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag == null) {
            return null;
        }
        return this.convertOuter(tag);
    }

    @Override
    public boolean exist(String name) {
        Assert.hasText(name, "tag name must not be blank.");
        return tagRepository.existsByName(name);
    }

    @Override
    public CategoryVO create(CategoryDTO dto) {
        Tag tag = new Tag();
        BeanCopier copier = BeanCopier.create(CategoryDTO.class, Tag.class, false);
        copier.copy(dto, tag, null);

        tag = tagRepository.saveAndFlush(tag);
        return this.convertOuter(tag);
    }

    @Override
    public CategoryVO modify(Long id, CategoryDTO dto) {
        Assert.notNull(id, "tag id must not be null.");
        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag == null) {
            return null;
        }
        BeanCopier copier = BeanCopier.create(CategoryDTO.class, Tag.class, false);
        copier.copy(dto, tag, null);

        tag = tagRepository.save(tag);
        return this.convertOuter(tag);
    }

    @Override
    public void remove(Long id) {
        Assert.notNull(id, "tag id must not be null.");

        tagRepository.deleteById(id);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param tag 信息
     * @return 输出转换后的vo对象
     */
    private CategoryVO convertOuter(Tag tag) {
        CategoryVO vo = new CategoryVO();
        BeanCopier copier = BeanCopier.create(Tag.class, CategoryVO.class, false);
        copier.copy(tag, vo, null);

        // get lastModifiedDate
        Optional<Instant> optionalInstant = tag.getLastModifiedDate();
        optionalInstant.ifPresent(vo::setLastModifiedDate);

        long count = postRepository.countByCategoryId(tag.getId());
        vo.setCount(count);
        return vo;
    }
}
