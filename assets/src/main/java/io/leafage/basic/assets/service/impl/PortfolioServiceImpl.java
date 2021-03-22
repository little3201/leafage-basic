/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Portfolio;
import io.leafage.basic.assets.dto.PortfolioDTO;
import io.leafage.basic.assets.repository.PortfolioRepository;
import io.leafage.basic.assets.service.PortfolioService;
import io.leafage.basic.assets.vo.PortfolioVO;
import io.leafage.common.basic.AbstractBasicService;
import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

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
    public Flux<PortfolioVO> retrieve(int page, int size) {
        return portfolioRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public Mono<Long> count() {
        return portfolioRepository.count();
    }

    @Override
    public Mono<PortfolioVO> create(PortfolioDTO portfolioDTO) {
        Portfolio info = new Portfolio();
        BeanUtils.copyProperties(portfolioDTO, info);
        info.setCode(this.generateCode());
        return portfolioRepository.insert(info).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<PortfolioVO> modify(String code, PortfolioDTO portfolioDTO) {
        Asserts.notBlank(code, "code");
        return portfolioRepository.getByCodeAndEnabledTrue(code).flatMap(portfolio -> {
            BeanUtils.copyProperties(portfolioDTO, portfolio);
            return portfolioRepository.save(portfolio).filter(Objects::nonNull).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> remove(String code) {
        Asserts.notBlank(code, "code");
        return portfolioRepository.getByCodeAndEnabledTrue(code).flatMap(article -> portfolioRepository.deleteById(article.getId()));
    }

    @Override
    public Mono<PortfolioVO> fetch(String code) {
        Asserts.notBlank(code, "code");
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
