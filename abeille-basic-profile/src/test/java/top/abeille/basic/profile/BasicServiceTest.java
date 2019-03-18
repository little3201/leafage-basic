package top.abeille.basic.profile;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * ServiceTest Parent
 *
 * @author liwenqiang 2018/12/28 14:40
 **/
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public abstract class BasicServiceTest {

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }
}

