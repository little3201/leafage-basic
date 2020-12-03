/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.Portfolio;
import top.abeille.basic.assets.dto.PortfolioDTO;
import top.abeille.basic.assets.repository.PortfolioRepository;
import top.abeille.basic.assets.service.PortfolioService;
import top.abeille.basic.assets.vo.PortfolioVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
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
    public Flux<PortfolioVO> retrieveAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return portfolioRepository.findAll(sort).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<PortfolioVO> create(PortfolioDTO portfolioDTO) {
        Portfolio info = new Portfolio();
        BeanUtils.copyProperties(portfolioDTO, info);
        info.setCode(this.generateCode());
        info.setEnabled(true);
        info.setModifyTime(LocalDateTime.now());
        return portfolioRepository.insert(info).filter(Objects::nonNull).map(this::convertOuter);
    }

    @Override
    public Mono<PortfolioVO> modify(String code, PortfolioDTO portfolioDTO) {
        Asserts.notBlank(code, "code");
        return portfolioRepository.findByCodeAndEnabledTrue(code).flatMap(portfolio -> {
            BeanUtils.copyProperties(portfolioDTO, portfolio);
            portfolio.setModifyTime(LocalDateTime.now());
            return portfolioRepository.save(portfolio).filter(Objects::nonNull).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> remove(String code) {
        Asserts.notBlank(code, "code");
        return portfolioRepository.findByCodeAndEnabledTrue(code).flatMap(article -> portfolioRepository.deleteById(article.getId()));
    }

    @Override
    public Mono<PortfolioVO> fetchByCode(String code) {
        Asserts.notBlank(code, "code");
        return portfolioRepository.findByCodeAndEnabledTrue(code).map(this::convertOuter);
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
