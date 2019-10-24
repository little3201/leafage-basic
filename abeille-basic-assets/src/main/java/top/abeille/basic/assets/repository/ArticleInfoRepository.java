/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.assets.entity.ArticleInfo;

/**
 * 文章基本信息dao
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
public interface ArticleInfoRepository extends JpaRepository<ArticleInfo, Long> {
}
