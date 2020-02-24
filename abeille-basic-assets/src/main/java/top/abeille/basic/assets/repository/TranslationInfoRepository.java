/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.assets.document.TranslationInfo;

/**
 * 翻译信息repository
 *
 * @author liwenqiang 2020/2/16 14:35
 **/
@Repository
public interface TranslationInfoRepository extends ReactiveMongoRepository<TranslationInfo, String> {
}
