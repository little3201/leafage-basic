/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.assets.api.HypervisorApi;
import top.abeille.basic.assets.bo.UserBO;
import top.abeille.basic.assets.document.ArticleDocument;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.repository.ArticleDocumentRepository;
import top.abeille.basic.assets.repository.ArticleInfoRepository;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.basic.assets.vo.ArticleVO;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * 文章信息service实现
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class ArticleInfoServiceImpl implements ArticleInfoService {

    private final ArticleInfoRepository articleInfoRepository;
    private final ArticleDocumentRepository articleDocumentRepository;
    private final HypervisorApi hypervisorApi;

    public ArticleInfoServiceImpl(ArticleInfoRepository articleInfoRepository, ArticleDocumentRepository articleDocumentRepository, HypervisorApi hypervisorApi) {
        this.articleInfoRepository = articleInfoRepository;
        this.articleDocumentRepository = articleDocumentRepository;
        this.hypervisorApi = hypervisorApi;
    }

    @Override
    public Page<ArticleVO> retrieveByPage(Pageable pageable) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher.withMatcher("is_enabled", exact());
        //设置查询必要参数，只查询is_enabled为true的数据
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setEnabled(true);
        Page<ArticleInfo> infoPage = articleInfoRepository.findAll(Example.of(articleInfo, exampleMatcher), pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(info -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(info, articleVO);
            UserBO user = this.fetchUser(info.getModifier());
            articleVO.setAuthor(user);
            return articleVO;
        });
    }

    @Override
    public ArticleVO fetchByBusinessId(String businessId) {
        //去mysql中查询基本信息
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setBusinessId(businessId);
        articleInfo.setEnabled(true);
        Optional<ArticleInfo> optional = articleInfoRepository.findOne(Example.of(articleInfo));
        if (optional.isEmpty()) {
            return null;
        }
        //去mongodb查询具体内容
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(optional.get(), articleVO);
        ArticleDocument document = new ArticleDocument();
        document.setBusinessId(businessId);
        Optional<ArticleDocument> documentOptional = articleDocumentRepository.findOne(Example.of(document));
        documentOptional.ifPresent(articleDocument -> articleVO.setContent(articleDocument.getContent()));
        return articleVO;
    }

    @Override
    @Transactional
    public ArticleVO create(ArticleDTO articleDTO) {
        ArticleInfo info = new ArticleInfo();
        info.setBusinessId(articleDTO.getBusinessId());
        //先查一下，是否已经存在，存在，填充主键id，不存在，填充业务id
        Optional<ArticleInfo> optional = articleInfoRepository.findOne(Example.of(info));
        BeanUtils.copyProperties(articleDTO, info);
        if (optional.isPresent()) {
            info.setId(optional.get().getId());
            info.setBusinessId(optional.get().getBusinessId());
        } else {
            //转换入参为数据对象，保存数据库
            info.setBusinessId("");
            info.setEnabled(true);
        }
        info.setModifyTime(LocalDateTime.now());
        articleInfoRepository.save(info);
        //保存文章内容
        ArticleDocument document = new ArticleDocument();
        document.setBusinessId(info.getBusinessId());
        //先查一下，是否已经存在，存在，填充主键id，不存在，填充业务id
        Optional<ArticleDocument> documentOptional = articleDocumentRepository.findOne(Example.of(document));
        documentOptional.ifPresent(articleDocument -> document.setId(articleDocument.getId()));
        document.setTitle(articleDTO.getTitle());
        document.setContent(articleDTO.getContent());
        articleDocumentRepository.save(document);
        //转换结果
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(info, articleVO);
        articleVO.setContent(articleDTO.getContent());
        return articleVO;
    }

    @Override
    public void removeById(String businessId) {
        Optional<ArticleInfo> optional = this.fetchInfo(businessId);
        if (optional.isPresent()) {
            ArticleInfo info = optional.get();
            articleInfoRepository.deleteById(info.getId());
        }
    }

    @Override
    public void removeInBatch(List<ArticleDTO> articleDTOs) {
    }

    /**
     * 根据业务ID查信息
     *
     * @param businessId 业务ID
     * @return 数据库对象信息
     */
    private Optional<ArticleInfo> fetchInfo(String businessId) {
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setBusinessId(businessId);
        return articleInfoRepository.findOne(Example.of(articleInfo));
    }

    private UserBO fetchUser(String businessId) {
        ResponseEntity<UserBO> responseEntity = hypervisorApi.fetchUser(businessId);
        return responseEntity.getBody();
    }
}
