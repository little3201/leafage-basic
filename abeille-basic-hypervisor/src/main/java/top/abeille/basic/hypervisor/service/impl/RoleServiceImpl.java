/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.document.RoleInfo;
import top.abeille.basic.hypervisor.document.RoleSource;
import top.abeille.basic.hypervisor.document.UserInfo;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.repository.RoleRepository;
import top.abeille.basic.hypervisor.repository.RoleSourceRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.RoleService;
import top.abeille.basic.hypervisor.service.SourceService;
import top.abeille.basic.hypervisor.service.UserService;
import top.abeille.basic.hypervisor.vo.RoleVO;
import top.abeille.common.basic.AbstractBasicService;

import java.rmi.NoSuchObjectException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色信息service 实现
 *
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleServiceImpl extends AbstractBasicService implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleSourceRepository roleSourceRepository;
    private final SourceService sourceService;
    private final UserService userService;

    public RoleServiceImpl(RoleRepository roleRepository, UserRoleRepository userRoleRepository, RoleSourceRepository roleSourceRepository,
                           SourceService sourceService, UserService userService) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleSourceRepository = roleSourceRepository;
        this.sourceService = sourceService;
        this.userService = userService;
    }

    @Override
    public Flux<RoleVO> retrieveAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return roleRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> fetchByBusinessId(String businessId) {
        return this.fetchInfo(businessId).map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> create(RoleDTO roleDTO) {
        RoleInfo info = new RoleInfo();
        BeanUtils.copyProperties(roleDTO, info);
        info.setBusinessId(PrefixEnum.RO + this.generateId());
        return roleRepository.insert(info).doOnNext(roleInfo -> {
            List<RoleSource> userRoleList = roleDTO.getSources().stream().map(source ->
                    this.initRoleSource(roleInfo.getId(), roleInfo.getModifier(), source)).collect(Collectors.toList());
            roleSourceRepository.saveAll(userRoleList).subscribe();
        }).map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> modify(String businessId, RoleDTO roleDTO) {
        return this.fetchInfo(businessId).flatMap(info -> {
            BeanUtils.copyProperties(roleDTO, info);
            return roleRepository.save(info).doOnNext(roleInfo -> {
                List<RoleSource> userRoleList = roleDTO.getSources().stream().map(source ->
                        this.initRoleSource(roleInfo.getId(), roleInfo.getModifier(), source)).collect(Collectors.toList());
                roleSourceRepository.saveAll(userRoleList).subscribe();
            });
        }).map(this::convertOuter);
    }

    @Override
    public Mono<ArrayList<String>> retrieveRoles(String businessId) {
        Mono<UserInfo> infoMono = userService.fetchInfo(businessId);
        Mono<ArrayList<String>> roleIdListMono = infoMono.switchIfEmpty(Mono.error(() -> new NoSuchObjectException("用户信息不存在")))
                .flatMap(userInfo -> userRoleRepository.findByUserId(userInfo.getId())
                        .collect(ArrayList::new, (roleIdList, userRole) -> roleIdList.add(userRole.getRoleId())));
        // 取角色关联的权限ID
        return roleIdListMono.flatMap(roleIdList -> roleRepository.findAllById(roleIdList)
                .collect(ArrayList::new, (roleList, role) -> roleList.add(role.getBusinessId())));
    }

    /**
     * 根据业务id查询
     *
     * @param businessId 业务id
     * @return 返回查询到的信息，否则返回empty
     */
    @Override
    public Mono<RoleInfo> fetchInfo(String businessId) {
        Objects.requireNonNull(businessId);
        RoleInfo info = new RoleInfo();
        info.setBusinessId(businessId);
        return roleRepository.findOne(Example.of(info));
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
     * @param roleId           角色主键
     * @param userBusinessId   用户业务ID
     * @param sourceBusinessId 资源业务ID
     * @return 用户-角色对象
     */
    private RoleSource initRoleSource(String roleId, String userBusinessId, String sourceBusinessId) {
        RoleSource roleSource = new RoleSource();
        roleSource.setRoleId(roleId);
        sourceService.fetchInfo(sourceBusinessId).subscribe(roleInfo -> roleSource.setRoleId(roleInfo.getId()));
        roleSource.setModifier(userBusinessId);
        roleSource.setModifyTime(LocalDateTime.now());
        return roleSource;
    }
}
