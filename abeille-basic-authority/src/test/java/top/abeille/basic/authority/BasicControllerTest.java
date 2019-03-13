package top.abeille.basic.authority;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Controller Parent
 *
 * @author liwenqiang 2018/12/28 14:40
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public abstract class BasicControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void exampleTest() {
        this.webClient.get().uri("/").exchange().expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello World");
    }
}
