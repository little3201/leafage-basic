package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Authority;
import io.leafage.basic.hypervisor.document.RoleAuthority;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.RoleAuthorityService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class RoleAuthorityServiceImpl implements RoleAuthorityService {

    private final RoleRepository roleRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final AuthorityRepository authorityRepository;

    public RoleAuthorityServiceImpl(RoleRepository roleRepository, RoleAuthorityRepository roleAuthorityRepository,
                                    AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Flux<String> authorities(String code) {
        return roleRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(group -> roleAuthorityRepository.findByRoleIdAndEnabledTrue(group.getId()).flatMap(roleAuthority ->
                        authorityRepository.findById(roleAuthority.getAuthorityId()).map(Authority::getCode))
                );
    }

    @Override
    public Flux<RoleVO> roles(String code) {
        return authorityRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(authority -> roleAuthorityRepository.findByAuthorityIdAndEnabledTrue(authority.getId()).flatMap(userRole ->
                        roleRepository.findById(userRole.getRoleId()).map(role -> {
                            RoleVO roleVO = new RoleVO();
                            BeanUtils.copyProperties(role, roleVO);
                            return roleVO;
                        })).switchIfEmpty(Mono.error(NoSuchElementException::new))
                );
    }

    @Override
    public Flux<RoleAuthority> relation(String code, Set<String> authorities) {
        Assert.hasText(code, "code is blank");
        Assert.notNull(authorities, "authorities is null");
        return roleRepository.getByCodeAndEnabledTrue(code).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMapMany(role -> {
                    RoleAuthority roleAuthority = new RoleAuthority();
                    roleAuthority.setRoleId(role.getId());
                    return authorityRepository.findByCodeInAndEnabledTrue(authorities).map(authority -> {
                                roleAuthority.setAuthorityId(authority.getId());
                                return roleAuthority;
                            }).switchIfEmpty(Mono.error(NoSuchElementException::new))
                            .collectList().flatMapMany(roleAuthorityRepository::saveAll);
                });
    }
}
