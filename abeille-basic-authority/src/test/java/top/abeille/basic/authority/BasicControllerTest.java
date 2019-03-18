package top.abeille.basic.authority;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

/**
 * Controller Parent
 *
 * @author liwenqiang 2018/12/28 14:40
 **/
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public abstract class BasicControllerTest<T> {

    private MockMvc mockMvc;

    protected abstract T getController();

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    @Before
    public void setup() {
        /* initialize mock object */
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.getController()).build();
    }

    /* ====================  POST  ====================*/
    public ResultActions postTest(String url, Object obj) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url).accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(obj)));
    }

    /* ====================  GET  ====================*/
    public ResultActions getTest(String url, Object obj) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(obj)));
    }

    /* ====================  GET  ====================*/
    public ResultActions getTestForPage(String url, MultiValueMap<String, String> params) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url).params(params).accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    /* ====================  PUT  ====================*/
    public ResultActions putTest(String url, Object obj) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(url).accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(obj)));
    }

    /* ====================  DELETE  ====================*/
    public ResultActions deleteTest(String url, Object obj) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url).accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(obj)));
    }
}
