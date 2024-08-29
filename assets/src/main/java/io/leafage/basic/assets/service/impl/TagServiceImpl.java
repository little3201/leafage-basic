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
import io.leafage.basic.assets.dto.TagDTO;
import io.leafage.basic.assets.repository.TagPostsRepository;
import io.leafage.basic.assets.repository.TagRepository;
import io.leafage.basic.assets.service.TagService;
import io.leafage.basic.assets.vo.TagVO;
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
    private final TagPostsRepository tagPostsRepository;

    public TagServiceImpl(TagRepository tagRepository, TagPostsRepository tagPostsRepository) {
        this.tagRepository = tagRepository;
        this.tagPostsRepository = tagPostsRepository;
    }

    @Override
    public Page<TagVO> retrieve(int page, int size, String sortBy) {
        Sort sort = Sort.by(StringUtils.hasText(sortBy) ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort.descending());
        return tagRepository.findAll(pageable).map(this::convert);
    }

    @Override
    public TagVO fetch(Long id) {
        Assert.notNull(id, "tag id must not be null.");
        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag == null) {
            return null;
        }
        return this.convert(tag);
    }

    @Override
    public boolean exist(String name) {
        Assert.hasText(name, "tag name must not be blank.");
        return tagRepository.existsByName(name);
    }

    @Override
    public TagVO create(TagDTO dto) {
        Tag tag = new Tag();
        BeanCopier copier = BeanCopier.create(TagDTO.class, Tag.class, false);
        copier.copy(dto, tag, null);

        tag = tagRepository.saveAndFlush(tag);
        return this.convert(tag);
    }

    @Override
    public TagVO modify(Long id, TagDTO dto) {
        Assert.notNull(id, "tag id must not be null.");
        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag == null) {
            return null;
        }
        BeanCopier copier = BeanCopier.create(TagDTO.class, Tag.class, false);
        copier.copy(dto, tag, null);

        tag = tagRepository.save(tag);
        return this.convert(tag);
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
    private TagVO convert(Tag tag) {
        TagVO vo = new TagVO();
        BeanCopier copier = BeanCopier.create(Tag.class, TagVO.class, false);
        copier.copy(tag, vo, null);

        // get lastModifiedDate
        Optional<Instant> optionalInstant = tag.getLastModifiedDate();
        optionalInstant.ifPresent(vo::setLastModifiedDate);

        long count = tagPostsRepository.countByTagId(tag.getId());
        vo.setCount(count);
        return vo;
    }
}
