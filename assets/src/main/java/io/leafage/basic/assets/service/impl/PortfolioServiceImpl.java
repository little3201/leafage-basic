/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Portfolio;
import io.leafage.basic.assets.dto.PortfolioDTO;
import io.leafage.basic.assets.repository.PortfolioRepository;
import io.leafage.basic.assets.service.PortfolioService;
import io.leafage.basic.assets.vo.PortfolioVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;

import javax.naming.NotContextException;

/**
 * 作品集信息service实现
 *
 * @author liwenqiang 2020/2/24 11:59
 **/
@Service
public class PortfolioServiceImpl extends AbstractBasicService implements PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public Flux<PortfolioVO> retrieve(int page, int size, String order) {
        return portfolioRepository.findByEnabledTrue(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,
                StringUtils.hasText(order) ? order : "modifyTime"))).map(this::convertOuter);
    }

    @Override
    public Mono<Long> count() {
        return portfolioRepository.count();
    }

    @Override
    public Mono<PortfolioVO> create(PortfolioDTO portfolioDTO) {
        Portfolio portfolio = new Portfolio();
        BeanUtils.copyProperties(portfolioDTO, portfolio);
        portfolio.setCode(this.generateCode());
        return portfolioRepository.insert(portfolio).map(this::convertOuter);
    }

    @Override
    public Mono<PortfolioVO> modify(String code, PortfolioDTO portfolioDTO) {
        Assert.hasText(code, "code is blank");
        return portfolioRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .map(portfolio -> {
                    BeanUtils.copyProperties(portfolioDTO, portfolio);
                    return portfolio;
                }).flatMap(portfolioRepository::save).map(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(String code) {
        Assert.hasText(code, "code is blank");
        return portfolioRepository.getByCodeAndEnabledTrue(code).flatMap(article -> portfolioRepository.deleteById(article.getId()));
    }

    @Override
    public Mono<PortfolioVO> fetch(String code) {
        Assert.hasText(code, "code is blank");
        return portfolioRepository.getByCodeAndEnabledTrue(code).map(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private PortfolioVO convertOuter(Portfolio info) {
        PortfolioVO outer = new PortfolioVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
