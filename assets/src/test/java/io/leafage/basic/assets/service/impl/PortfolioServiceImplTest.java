/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Portfolio;
import io.leafage.basic.assets.dto.PortfolioDTO;
import io.leafage.basic.assets.repository.PortfolioRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.BDDMockito.given;

/**
 * portfolio service测试
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(MockitoExtension.class)
class PortfolioServiceImplTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private PortfolioServiceImpl portfolioService;

    @Test
    void retrieve() {
        given(this.portfolioRepository.findByEnabledTrue(PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(Mockito.mock(Portfolio.class)));
        StepVerifier.create(this.portfolioService.retrieve(0, 2, "id")).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Mockito.when(this.portfolioRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .thenReturn(Mono.just(Mockito.mock(Portfolio.class)));
        StepVerifier.create(portfolioService.fetch("21318H9FH")).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        given(this.portfolioRepository.count()).willReturn(Mono.just(2L));
        StepVerifier.create(portfolioService.count()).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.portfolioRepository.existsByTitle(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(portfolioService.exist("test")).expectNext(Boolean.TRUE).verifyComplete();
    }

    @Test
    void create() {
        PortfolioDTO portfolioDTO = new PortfolioDTO();
        portfolioDTO.setTitle("test");
        given(this.portfolioRepository.insert(Mockito.any(Portfolio.class))).willReturn(Mono.just(Mockito.mock(Portfolio.class)));

        StepVerifier.create(portfolioService.create(portfolioDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(new ObjectId());
        given(this.portfolioRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(portfolio));

        given(this.portfolioRepository.save(Mockito.any(Portfolio.class))).willReturn(Mono.just(Mockito.mock(Portfolio.class)));

        PortfolioDTO portfolioDTO = new PortfolioDTO();
        portfolioDTO.setTitle("test");
        StepVerifier.create(portfolioService.modify("21318H9FH", portfolioDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(new ObjectId());
        given(this.portfolioRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(portfolio));

        given(this.portfolioRepository.deleteById(Mockito.any(ObjectId.class))).willReturn(Mono.empty());

        StepVerifier.create(portfolioService.remove("21318H9FH")).verifyComplete();
    }
}