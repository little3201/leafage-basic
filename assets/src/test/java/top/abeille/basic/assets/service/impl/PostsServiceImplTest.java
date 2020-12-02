package top.abeille.basic.assets.service.impl;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.PostsDTO;
import top.abeille.basic.assets.service.PostsService;
import top.abeille.basic.assets.vo.PostsVO;

import java.util.Random;

/**
 * 文章接口测试类
 *
 * @author liwenqiang 2019/9/19 9:27
 */
@SpringBootTest
public class PostsServiceImplTest {

    @Autowired
    private PostsService postsService;

    @Test
    @Rollback
    public void create() {
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("spring");
        postsDTO.setContent("spring boot");
        Mono<PostsVO> outerMono = postsService.create(postsDTO);
        Assert.notNull(outerMono.block(), "The class must not be null");
    }

    @Test
    public void fetchById_returnObject() {
        Mono<? extends PostsVO> outerMono = postsService.fetchByCode("AT226");
        Assert.notNull(outerMono.block(), "The class must not be null");
    }

    @Test
    public void fetchById_returnNull() {
        Mono<? extends PostsVO> outerMono = postsService.fetchByCode(String.valueOf(new Random().nextFloat()));
        Assert.isNull(outerMono.block(), "The class must be null");
    }
}