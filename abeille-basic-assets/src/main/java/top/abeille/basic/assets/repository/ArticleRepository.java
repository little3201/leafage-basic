package top.abeille.basic.assets.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import top.abeille.basic.assets.document.Article;

/**
 * 文章es存储接口
 *
 * @author liwenqiang 2019-08-20 22:25
 **/
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {
}
