package top.abeille.basic.assets.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import top.abeille.basic.assets.entity.Posts;
import top.abeille.basic.assets.repository.PostsRepository;

/**
 * 文章接口测试
 *
 * @author liwenqiang 2019-08-20 22:38
 **/
@SpringBootTest
public class PostsServiceImplTest {

    @Mock
    private PostsRepository postsRepository;

    @Test
    public void save() {
        Mockito.when(postsRepository.save(Mockito.any())).thenAnswer(Mockito.mock(Answers.class));
        Posts result = postsRepository.save(Mockito.mock(Posts.class));
        Mockito.verify(postsRepository, Mockito.atMostOnce()).save(Mockito.mock(Posts.class));
        Assertions.assertNotNull(result.getId());
    }

}
