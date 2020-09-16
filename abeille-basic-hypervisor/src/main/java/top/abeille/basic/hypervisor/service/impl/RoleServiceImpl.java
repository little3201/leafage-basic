/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.document.RoleInfo;
import top.abeille.basic.hypervisor.document.RoleSource;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.repository.RoleRepository;
import top.abeille.basic.hypervisor.repository.RoleSourceRepository;
import top.abeille.basic.hypervisor.service.RoleService;
import top.abeille.basic.hypervisor.service.SourceService;
import top.abeille.basic.hypervisor.vo.RoleVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息service 实现
 *
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleServiceImpl extends AbstractBasicService implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleSourceRepository roleSourceRepository;
    private final SourceService sourceService;

    public RoleServiceImpl(RoleRepository roleRepository, RoleSourceRepository roleSourceRepository, SourceService sourceService) {
        this.roleRepository = roleRepository;
        this.roleSourceRepository = roleSourceRepository;
        this.sourceService = sourceService;
    }

    @Override
    public Flux<RoleVO> retrieveAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return roleRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> fetchByCode(String code) {
        return roleRepository.findByCodeAndEnabledTrue(code).map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> create(RoleDTO roleDTO) {
        RoleInfo info = new RoleInfo();
        BeanUtils.copyProperties(roleDTO, info);
        info.setCode(PrefixEnum.RO + this.generateId());
        return roleRepository.insert(info).doOnNext(roleInfo -> {
            List<RoleSource> userRoleList = roleDTO.getSources().stream().map(source ->
                    this.initRoleSource(roleInfo.getId(), roleInfo.getModifier(), source)).collect(Collectors.toList());
            roleSourceRepository.saveAll(userRoleList).subscribe();
        }).map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> modify(String code, RoleDTO roleDTO) {
        return roleRepository.findByCodeAndEnabledTrue(code).flatMap(info -> {
            BeanUtils.copyProperties(roleDTO, info);
            return roleRepository.save(info).doOnNext(roleInfo -> {
                List<RoleSource> userRoleList = roleDTO.getSources().stream().map(source ->
                        this.initRoleSource(roleInfo.getId(), roleInfo.getModifier(), source)).collect(Collectors.toList());
                roleSourceRepository.saveAll(userRoleList).subscribe();
            });
        }).map(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private RoleVO convertOuter(RoleInfo info) {
        RoleVO outer = new RoleVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }

    /**
     * 初始设置UserRole参数
     *
     * @param roleId   角色主键
     * @param modifier 修改人
     * @param sourceId 资源ID
     * @return 用户-角色对象
     */
    private RoleSource initRoleSource(String roleId, String modifier, String sourceId) {
        RoleSource roleSource = new RoleSource();
        roleSource.setRoleId(roleId);
        roleSource.setModifier(modifier);
        roleSource.setModifyTime(LocalDateTime.now());
        sourceService.findByIdAndEnabledTrue(sourceId).doOnNext(sourceInfo ->
                roleSource.setSourceId(sourceInfo.getId())).subscribe();
        return roleSource;
    }

    @Override
    public Mono<RoleInfo> findByCodeAndEnabledTrue(String code) {
        return roleRepository.findByCodeAndEnabledTrue(code);
    }
}
