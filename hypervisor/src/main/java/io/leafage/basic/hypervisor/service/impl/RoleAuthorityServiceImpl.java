package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Authority;
import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.entity.RoleAuthority;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.RoleAuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import top.leafage.common.basic.ValidMessage;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * role authority service impl.
 *
 * @author liwenqiang 2021/9/27 14:18
 **/
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
    public List<AuthorityVO> authorities(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Role role = roleRepository.getByCodeAndEnabledTrue(code);
        if (role == null) {
            return Collections.emptyList();
        }
        List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findByRoleId(role.getId());
        if (CollectionUtils.isEmpty(roleAuthorities)) {
            return Collections.emptyList();
        }
        return roleAuthorities.stream().map(roleAuthority -> authorityRepository.findById(roleAuthority.getAuthorityId()))
                .map(Optional::orElseThrow).map(authority -> {
                    AuthorityVO authorityVO = new AuthorityVO();
                    BeanUtils.copyProperties(authority, authorityVO);
                    return authorityVO;
                }).toList();
    }

    @Override
    public List<RoleVO> roles(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Authority authority = authorityRepository.getByCodeAndEnabledTrue(code);
        if (authority == null) {
            return Collections.emptyList();
        }
        List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findByAuthorityId(authority.getId());
        if (CollectionUtils.isEmpty(roleAuthorities)) {
            return Collections.emptyList();
        }
        return roleAuthorities.stream().map(roleAuthority -> roleRepository.findById(roleAuthority.getRoleId()))
                .map(Optional::orElseThrow).map(role -> {
                    RoleVO roleVO = new RoleVO();
                    BeanUtils.copyProperties(role, roleVO);
                    return roleVO;
                }).toList();
    }

    @Override
    public List<RoleAuthority> relation(String code, Set<String> authorities) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        Assert.notNull(authorities, "authorities is empty.");
        Role role = roleRepository.getByCodeAndEnabledTrue(code);
        if (role == null) {
            return Collections.emptyList();
        }
        List<RoleAuthority> roleAuthorities = authorities.stream().map(s -> {
            Authority authority = authorityRepository.getByCodeAndEnabledTrue(s);
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setRoleId(role.getId());
            roleAuthority.setAuthorityId(authority.getId());
            return roleAuthority;
        }).toList();
        return roleAuthorityRepository.saveAllAndFlush(roleAuthorities);
    }
}
