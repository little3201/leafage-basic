package top.abeille.basic.assets.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.abeille.basic.assets.document.Article;
import top.abeille.basic.assets.repository.ArticleRepository;

/**
 * 文章接口测试
 *
 * @author liwenqiang 2019-08-20 22:38
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleInfoServiceImplTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void getByArticleId() {
    }

    @Test
    public void saveArticle() {
        Article article = new Article();
        article.setArticleId("002");
        String content = "统一返回值\n" +
                "在前后端分离大行其道的今天，有一个统一的返回值格式不仅能使我们的接口看起来更漂亮，而且还可以使前端可以统一处理很多东西，避免很多问题的产生。\n" +
                "\n" +
                "比较通用的返回值格式如下：\n" +
                "\n" +
                "public class Result<T> {\n" +
                "    // 接口调用成功或者失败\n" +
                "    private Integer code = 0;\n" +
                "    // 失败的具体code\n" +
                "    private String errorCode = \"\";\n" +
                "    // 需要传递的信息，例如错误信息\n" +
                "    private String msg;\n" +
                "    // 需要传递的数据\n" +
                "    private T data;\n" +
                "    ...\n" +
                "}\n" +
                "最原始的接口如下：\n" +
                "\n" +
                "    @GetMapping(\"/test\")\n" +
                "    public User test() {\n" +
                "        return new User();\n" +
                "    }\n" +
                "当我们需要统一返回值时，可能会使用这样一个办法：\n" +
                "\n" +
                "    @GetMapping(\"/test\")\n" +
                "    public Result test() {\n" +
                "        return Result.success(new User());\n" +
                "    }\n" +
                "这个方法确实达到了统一接口返回值的目的，但是却有几个新问题诞生了：\n" +
                "\n" +
                "接口返回值不明显，不能一眼看出来该接口的返回值。\n" +
                "每一个接口都需要增加额外的代码量。\n" +
                "所幸Spring Boot已经为我们提供了更好的解决办法，只需要在项目中加上以下代码，就可以无感知的为我们统一全局返回值。\n" +
                "\n" +
                "/**\n" +
                " * 全局返回值统一封装\n" +
                " */\n" +
                "@EnableWebMvc\n" +
                "@Configuration\n" +
                "public class GlobalReturnConfig {\n" +
                "\n" +
                "    @RestControllerAdvice\n" +
                "    static class ResultResponseAdvice implements ResponseBodyAdvice<Object> {\n" +
                "        @Override\n" +
                "        public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {\n" +
                "            return true;\n" +
                "        }\n" +
                "\n" +
                "        @Override\n" +
                "        public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {\n" +
                "            if (body instanceof Result) {\n" +
                "                return body;\n" +
                "            }\n" +
                "            return new Result(body);\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "\n" +
                "而我们的接口只需要写成最原始的样子就行了。\n" +
                "\n" +
                "    @GetMapping(\"/test\")\n" +
                "    public User test() {\n" +
                "        return new User();\n" +
                "    }\n" +
                "统一处理异常\n" +
                "将返回值统一封装时我们没有考虑当接口抛出异常的情况。当接口抛出异常时让用户直接看到服务端的异常肯定是不够友好的，而我们也不可能每一个接口都去try/catch进行处理，此时只需要使用@ExceptionHandler注解即可无感知的全局统一处理异常。\n" +
                "\n" +
                "@RestControllerAdvice\n" +
                "public class GlobalExceptionHandler {\n" +
                "\n" +
                "    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);\n" +
                "\n" +
                "    /**\n" +
                "     * 全局异常处理\n" +
                "     */\n" +
                "    @ExceptionHandler\n" +
                "    public JsonData handleException(HttpServletRequest request, HttpServletResponse response, final Exception e) {\n" +
                "        LOG.error(e.getMessage(), e);\n" +
                "        if (e instanceof AlertException) {//可以在前端Alert的异常\n" +
                "            if (((AlertException) e).getRetCode() != null) {//预定义异常\n" +
                "                return new Result(((AlertException) e).getRetCode());\n" +
                "            } else {\n" +
                "                return new Result(1, e.getMessage() != null ? e.getMessage() : \"\");\n" +
                "            }\n" +
                "        } else {//其它异常\n" +
                "            if (Util.isProduct()) {//如果是正式环境，统一提示\n" +
                "                return new Result(RetCode.ERROR);\n" +
                "            } else {//测试环境，alert异常信息\n" +
                "                return new Result(1, StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : e.toString());\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "}\n" +
                "其中的AlertException为我们自定义的异常，因此当业务中需要抛出错误时，可以手动抛出AlertException。\n" +
                "\n" +
                "以上就是统一处理返回值和统一处理异常的两步。";
        article.setContent(content);
        articleRepository.save(article);
    }
}
