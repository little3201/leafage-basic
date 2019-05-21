package top.abeille.basic.authority.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置
 *
 * @author liwenqiang 2018/12/21 9:57
 **/
@Configuration
@EnableSwagger2
public class BasicSwaggerConfig {

    @Bean
    public Docket createRestApi() {
        // 添加请求参数
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Basic Module of Abeille").version("0.0.1")
                .description("Provide security service for Abeille")
                .contact(new Contact("wilson", "https://www.abeille.top", "little3201@163.com"))
                .build();
    }
}
