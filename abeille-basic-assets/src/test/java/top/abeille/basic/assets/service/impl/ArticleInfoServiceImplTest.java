package top.abeille.basic.assets.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.repository.ArticleInfoRepository;

/**
 * 文章接口测试
 *
 * @author liwenqiang 2019-08-20 22:38
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleInfoServiceImplTest {

    @Autowired
    private ArticleInfoRepository articleInfoRepository;

    @Test
    public void saveArticle() {
        ArticleInfo article = new ArticleInfo();
        article.setArticleId("002");
        String details = "# edc-sdk-logger\n" +
                "\n" +
                "elasticsearch 记录的简单日志服务：提供日志保存接口，范围查询接口，多条件查询接口，都支持分页；\n" +
                "\n" +
                "\n" +
                "### 使用方法：\n" +
                "\n" +
                "1、添加依赖：版本已经加入到 framework-parent 的 pom 中；\n" +
                "\n" +
                "```\n" +
                "<dependency>\n" +
                "    <groupId>com.edc.sdk</groupId>\n" +
                "    <artifactId>edc-sdk-logger</artifactId>\n" +
                "</dependency>\n" +
                "```\n" +
                "2、在启动类上加入@EnableFeignClients(\"com.edc.sdk.logger\"),如果有这个注解，则将\"com.edc.sdk.logger\"添加到后面即可\n" +
                "\n" +
                "示例：\n" +
                "\n" +
                "```$xslt\n" +
                "@EnableFeignClients(basePackages = {\"com.example\",\"com.edc.sdk.logger\"})\n" +
                "```\n" +
                "\n" +
                "3、注入LogServiceApi 接口，此接口为feign 接口\n" +
                "\n" +
                "```\n" +
                "    @Autowired\n" +
                "    private LogServerApi logServerApi;\n" +
                "```\n" +
                "\n" +
                "### 接口说明：\n" +
                "\n" +
                "- 保存日志信息\n" +
                "\n" +
                "```\n" +
                "    /**\n" +
                "     * 保存日志信息\n" +
                "     *\n" +
                "     * @param businessLog 日志信息\n" +
                "     * @return BusinessLog\n" +
                "     */\n" +
                "    @PostMapping(\"/logger/save\")\n" +
                "    Response<BusinessLog> saveLog(@RequestBody BusinessLog businessLog);\n" +
                "```\n" +
                "    \n" +
                "- 多条件匹配查询，字段查询精确匹配（相当与 == ）\n" +
                "\n" +
                "```\n" +
                "    /**\n" +
                "     * 多条件匹配查询\n" +
                "     *\n" +
                "     * @param businessLogVO 查询参数\n" +
                "     * @param pageNum       页码\n" +
                "     * @param pageSize      分页大小\n" +
                "     * @return BusinessLog\n" +
                "     */\n" +
                "    @PostMapping(\"/logger/query\")\n" +
                "    Response<Page<BusinessLog>> boolQueryByPage(@RequestBody BusinessLogVO businessLogVO);\n" +
                "```\n" +
                "\n" +
                "### 参数说明\n" +
                "    \n" +
                "+ BusinessLog : 日志操作模板，其中appId 必填\n" +
                "\n" +
                "```\n" +
                "    id:             日志记录id，即主键\n" +
                "    appId:          应用标记，一个微服务应用唯一\n" +
                "    title:          日志标题\n" +
                "    globalNo:       全局流水号，需保证全局唯一，用于表示一个完整的生命周期记录\n" +
                "    globalType:     全局流水类型，用于表示一个完整的生命周期记录\n" +
                "    businessNo:     业务流水号，业务域内唯一，用于表示服务内部接收到请求后的一次完整的处理过程\n" +
                "    businessType:   业务类型，业务域内的请求类型\n" +
                "    content:        日志内容\n" +
                "    createTime:     日志创建时间\n" +
                "    creator:        操作人\n" +
                "```\n" +
                "\n" +
                "+ BusinessLogIn : 日志查询接口入参，继承 PageIn，其中appId 必填\n" +
                "\n" +
                "```\n" +
                "    appId:          应用标记，一个微服务应用唯一\n" +
                "    title:          日志标题\n" +
                "    globalNo:       全局流水号，需保证全局唯一，用于表示一个完整的生命周期记录\n" +
                "    globalType:     全局流水类型，用于表示一个完整的生命周期记录\n" +
                "    businessNo:     业务流水号，业务域内唯一，用于表示服务内部接收到请求后的一次完整的处理过程\n" +
                "    businessType:   业务类型，业务域内的请求类型\n" +
                "    startDate:      起始日期\n" +
                "    endDate:        截止日期\n" +
                "    \n" +
                "    #继承自PageIn\n" +
                "    pageNum:        查询页码\n" +
                "    pageSize:       分页大小\n" +
                "    orderBy:        排序字段（暂时无效）\n" +
                "```";
        article.setContent(details);
        ArticleInfo result = articleInfoRepository.save(article);
        Assert.assertNotNull(result.getContent());
    }
}
