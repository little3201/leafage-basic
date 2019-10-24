/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.assets.document.ArticleDocument;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.repository.ArticleDocumentRepository;
import top.abeille.basic.assets.repository.ArticleInfoRepository;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.basic.assets.vo.ArticleVO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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

    public ArticleInfoServiceImpl(ArticleInfoRepository articleInfoRepository, ArticleDocumentRepository articleDocumentRepository) {
        this.articleInfoRepository = articleInfoRepository;
        this.articleDocumentRepository = articleDocumentRepository;
    }

    @Override
    public Page<ArticleVO> fetchByPage(Pageable pageable) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher.withMatcher("is_enabled", exact());
        //设置查询必要参数，只查询is_enabled为true的数据
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setEnabled(true);
        Page<ArticleInfo> infoPage = articleInfoRepository.findAll(Example.of(articleInfo, exampleMatcher), pageable);
        List<ArticleInfo> infoList = infoPage.getContent();
        if (CollectionUtils.isEmpty(infoList)) {
            return new PageImpl<>(Collections.emptyList());
        }
        //参数转换为出参结果
        List<ArticleVO> voList = new ArrayList<>(infoList.size());
        for (ArticleInfo info : infoList) {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(info, articleVO);
            voList.add(articleVO);
        }
        Page<ArticleVO> voPage = new PageImpl<>(voList);
        BeanUtils.copyProperties(infoPage, voPage);
        return voPage;
    }

    @Override
    public ArticleVO queryById(Long articleId) {
        //去mysql中查询基本信息
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setArticleId(articleId);
        articleInfo.setEnabled(true);
        Optional<ArticleInfo> optional = articleInfoRepository.findOne(Example.of(articleInfo));
        if (!optional.isPresent()) {
            return null;
        }
        //去mongodb查询具体内容
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(optional.get(), articleVO);
        ArticleDocument document = new ArticleDocument();
        document.setArticleId(articleId);
        Optional<ArticleDocument> documentOptional = articleDocumentRepository.findOne(Example.of(document));
        documentOptional.ifPresent(articleDocument -> articleVO.setContent(articleDocument.getContent()));
        return articleVO;
    }

    @Override
    @Transactional
    public ArticleVO save(ArticleDTO articleDTO) {
        ArticleInfo info = new ArticleInfo();
        info.setArticleId(articleDTO.getArticleId());
        //先查一下，是否已经存在，存在，填充主键id，不存在，填充业务id
        Optional<ArticleInfo> optional = articleInfoRepository.findOne(Example.of(info));
        BeanUtils.copyProperties(articleDTO, info);
        Long articleId;
        if (optional.isPresent()) {
            info.setId(optional.get().getId());
            info.setArticleId(optional.get().getArticleId());
        } else {
            //获得时间作为文章id
            articleId = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
            //转换入参为数据对象，保存数据库
            info.setArticleId(articleId);
            info.setEnabled(true);
        }
        info.setModifier(0L);
        articleInfoRepository.save(info);
        //保存文章内容
        ArticleDocument document = new ArticleDocument();
        document.setArticleId(info.getArticleId());
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
    public void removeById(Long articleId) {

    }

    @Override
    public void removeInBatch(List<ArticleDTO> articleDTOs) {

    }
}
