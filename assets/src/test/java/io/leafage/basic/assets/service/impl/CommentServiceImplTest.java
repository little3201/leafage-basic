package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Comment;
import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * comment service测试
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void retrieve() {
        given(this.commentRepository.findByEnabledTrue(PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id"))))
                .willReturn(Flux.just(Mockito.mock(Comment.class)));

        StepVerifier.create(commentService.retrieve(0, 2, "id"))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.commentRepository.insert(Mockito.any(Comment.class)))
                .willReturn(Mono.just(Mockito.mock(Comment.class)));

        StepVerifier.create(commentService.create(Mockito.mock(CommentDTO.class)))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.commentRepository.getByCodeAndEnabledTrue(Mockito.anyString())).
                willReturn(Mono.just(Mockito.mock(Comment.class)));

        given(this.commentRepository.save(Mockito.any(Comment.class)))
                .willReturn(Mono.just(Mockito.mock(Comment.class)));

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("测试");
        StepVerifier.create(commentService.modify("21318H9FH", commentDTO))
                .expectNextCount(1).verifyComplete();
    }
}