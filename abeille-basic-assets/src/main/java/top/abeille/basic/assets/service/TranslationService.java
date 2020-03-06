/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.TranslationDTO;
import top.abeille.basic.assets.vo.TranslationDetailsVO;
import top.abeille.basic.assets.vo.TranslationVO;
import top.abeille.common.basic.BasicService;

/**
 * 翻译信息Service
 *
 * @author liwenqiang 2020/2/13 20:16
 **/
public interface TranslationService extends BasicService<TranslationDTO, TranslationVO> {

    /**
     * 根据业务id查询详细信息
     *
     * @param businessId 业务id
     * @return 详细信息
     */
    Mono<TranslationDetailsVO> fetchDetailsByBusinessId(String businessId);
}
