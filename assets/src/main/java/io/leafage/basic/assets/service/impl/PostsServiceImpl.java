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

import io.leafage.basic.assets.domain.Post;
import io.leafage.basic.assets.domain.PostContent;
import io.leafage.basic.assets.domain.Tag;
import io.leafage.basic.assets.domain.TagPosts;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.repository.PostContentRepository;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.repository.TagPostsRepository;
import io.leafage.basic.assets.repository.TagRepository;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * posts service impl.
 *
 * @author wq li  2018-12-20 9:54
 **/
@Service
public class PostsServiceImpl implements PostsService {

    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;
    private final TagRepository tagRepository;
    private final TagPostsRepository tagPostsRepository;

    public PostsServiceImpl(PostRepository postRepository, PostContentRepository postContentRepository,
                            TagRepository tagRepository, TagPostsRepository tagPostsRepository) {
        this.postRepository = postRepository;
        this.postContentRepository = postContentRepository;
        this.tagRepository = tagRepository;
        this.tagPostsRepository = tagPostsRepository;
    }

    @Override
    public Page<PostVO> retrieve(int page, int size, String sortBy) {
        Sort sort = Sort.by(StringUtils.hasText(sortBy) ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort.descending());
        return postRepository.findAll(pageable).map(this::convert);
    }

    @Override
    public PostVO fetch(Long id) {
        Assert.notNull(id, "post id must not be null.");
        //查询基本信息
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }
        return this.convert(post);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PostVO details(Long id) {
        Assert.notNull(id, "post id must not be null.");
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }
        PostVO vo = this.convert(post);
        // 获取内容详情
        PostContent postContent = postContentRepository.getByPostId(id);
        if (postContent != null) {
            vo.setContent(postContent.getContent());
        }
        return vo;
    }

    @Override
    public boolean exist(String title) {
        return postRepository.existsByTitle(title);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PostVO create(PostDTO dto) {
        Post post = new Post();
        BeanCopier copier = BeanCopier.create(PostDTO.class, Post.class, false);
        copier.copy(dto, post, null);

        // 保存并立即刷盘
        post = postRepository.saveAndFlush(post);
        //保存帖子内容
        PostContent postContent = postContentRepository.getByPostId(post.getId());
        if (postContent == null) {
            postContent = new PostContent();
        }
        postContent.setPostId(post.getId());
        postContent.setContent(dto.getContent());
        postContentRepository.saveAndFlush(postContent);

        // sava tag
        this.relation(post.getId(), dto.getTags());
        //转换结果
        return this.convert(post);
    }

    @Override
    public PostVO modify(Long id, PostDTO dto) {
        Assert.notNull(id, "post id must not be null.");
        //查询基本信息
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }
        BeanCopier copier = BeanCopier.create(PostDTO.class, Post.class, false);
        copier.copy(dto, post, null);

        post = postRepository.save(post);

        //保存文章内容
        PostContent postContent = postContentRepository.getByPostId(id);
        if (postContent == null) {
            postContent = new PostContent();
        }
        postContent.setContent(dto.getContent());
        postContentRepository.save(postContent);

        // sava tag
        this.relation(post.getId(), dto.getTags());

        //转换结果
        return this.convert(post);
    }

    @Override
    public void remove(Long id) {
        Assert.notNull(id, "post id must not be null.");

        postRepository.deleteById(id);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param post 信息
     * @return 输出转换后的vo对象
     */
    private PostVO convert(Post post) {
        PostVO vo = new PostVO();
        BeanCopier copier = BeanCopier.create(Post.class, PostVO.class, false);
        copier.copy(post, vo, null);

        // get lastModifiedDate
        Optional<Instant> optionalInstant = post.getLastModifiedDate();
        optionalInstant.ifPresent(vo::setLastModifiedDate);

        List<TagPosts> tagPostsList = tagPostsRepository.findByPostId(post.getId());
        // 转换 tags
        if (!CollectionUtils.isEmpty(tagPostsList)) {
            Set<String> tags = tagPostsList.stream().map(tagPosts -> {
                Optional<Tag> optional = tagRepository.findById(tagPosts.getId());
                return optional.map(Tag::getName).orElse(null);
            }).collect(Collectors.toSet());

            vo.setTags(tags);
        }
        return vo;
    }

    private void relation(Long postId, Set<String> tags) {
        tagPostsRepository.deleteByPostId(postId);
        // 设置新的关联
        if (!CollectionUtils.isEmpty(tags)) {
            List<TagPosts> tagPostsList = new ArrayList<>(tags.size());
            tags.forEach(t -> {
                Tag tag = tagRepository.getByName(t);
                if (tag == null) {
                    Tag newTag = new Tag();
                    newTag.setName(t);
                    tag = tagRepository.saveAndFlush(newTag);
                }

                TagPosts tagPosts = new TagPosts();
                tagPosts.setPostId(postId);
                tagPosts.setTagId(tag.getId());
                tagPostsList.add(tagPosts);
            });
            tagPostsRepository.saveAll(tagPostsList);
        }
    }

}
