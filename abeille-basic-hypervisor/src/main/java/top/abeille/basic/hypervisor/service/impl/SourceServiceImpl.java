/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.apache.http.util.Asserts;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.constant.SourceTypeEnum;
import top.abeille.basic.hypervisor.document.SourceInfo;
import top.abeille.basic.hypervisor.dto.SourceDTO;
import top.abeille.basic.hypervisor.repository.SourceRepository;
import top.abeille.basic.hypervisor.service.SourceService;
import top.abeille.basic.hypervisor.vo.SourceVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.Objects;

/**
 * 权限资源信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class SourceServiceImpl extends AbstractBasicService implements SourceService {

    private final SourceRepository sourceRepository;

    public SourceServiceImpl(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    @Override
    public Flux<SourceVO> retrieveAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return sourceRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<SourceVO> fetchByBusinessId(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        return this.fetchInfo(businessId).map(this::convertOuter);
    }

    @Override
    public Mono<SourceVO> create(SourceDTO sourceDTO) {
        SourceInfo info = new SourceInfo();
        BeanCopier.create(SourceDTO.class, SourceInfo.class, false).copy(sourceDTO, info, null);
        String prefix;
        switch (SourceTypeEnum.valueOf(sourceDTO.getType())) {
            case MENU:
                prefix = PrefixEnum.SM.name(); // 菜单
                break;
            case BUTTON:
                prefix = PrefixEnum.SB.name(); // 按钮
                break;
            case TAB:
                prefix = PrefixEnum.ST.name(); // tab页
                break;
            default:
                prefix = PrefixEnum.SU.name(); // 路径
        }
        info.setBusinessId(prefix + this.generateId());
        info.setEnabled(Boolean.TRUE);
        return sourceRepository.insert(info).map(this::convertOuter);
    }

    @Override
    public Mono<SourceVO> modify(String businessId, SourceDTO sourceDTO) {
        Objects.requireNonNull(businessId);
        return this.fetchInfo(businessId).flatMap(info -> {
            BeanCopier.create(SourceDTO.class, SourceInfo.class, false).copy(sourceDTO, info, null);
            return sourceRepository.save(info);
        }).map(this::convertOuter);
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    @Override
    public Mono<SourceInfo> fetchInfo(String businessId) {
        Asserts.notBlank(businessId, "businessId");
        SourceInfo info = new SourceInfo();
        info.setBusinessId(businessId);
        return sourceRepository.findOne(Example.of(info));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private SourceVO convertOuter(SourceInfo info) {
        SourceVO outer = new SourceVO();
        BeanCopier.create(SourceInfo.class, SourceVO.class, false).copy(info, outer, null);
        return outer;
    }
}
