package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Role;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.repository.RoleMembersRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import top.leafage.common.TreeNode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * role service test
 *
 * @author wq li 2021/5/11 10:10
 **/
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleMembersRepository roleMembersRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private RoleDTO roleDTO;

    @BeforeEach
    void init() {
        roleDTO = new RoleDTO();
        roleDTO.setName("role");
        roleDTO.setDescription("role");
        roleDTO.setAuthorities(Set.of("add"));
        roleDTO.setSuperiorId(1L);
    }

    @Test
    void retrieve() {
        Page<Role> page = new PageImpl<>(List.of(Mockito.mock(Role.class)));
        given(this.roleRepository.findAll(PageRequest.of(0, 2, Sort.by("id")))).willReturn(page);

        given(this.roleMembersRepository.countByRoleIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.anyLong());

        Page<RoleVO> voPage = roleService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.roleRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Role.class)));

        given(this.roleMembersRepository.countByRoleIdAndEnabledTrue(Mockito.anyLong())).willReturn(2L);

        RoleVO roleVO = roleService.fetch(Mockito.anyLong());

        Assertions.assertNotNull(roleVO);
    }

    @Test
    void create() {
        given(this.roleRepository.saveAndFlush(Mockito.any(Role.class))).willReturn(Mockito.mock(Role.class));

        given(this.roleMembersRepository.countByRoleIdAndEnabledTrue(Mockito.anyLong())).willReturn(2L);

        RoleVO roleVO = roleService.create(Mockito.mock(RoleDTO.class));

        verify(this.roleRepository, times(1)).saveAndFlush(Mockito.any(Role.class));
        Assertions.assertNotNull(roleVO);
    }

    @Test
    void modify() {
        given(this.roleRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Role.class)));

        given(this.roleRepository.save(Mockito.any(Role.class))).willReturn(Mockito.mock(Role.class));

        given(this.roleMembersRepository.countByRoleIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.anyLong());

        RoleVO roleVO = roleService.modify(1L, roleDTO);

        verify(this.roleRepository, times(1)).save(Mockito.any(Role.class));
        Assertions.assertNotNull(roleVO);
    }

    @Test
    void remove() {
        roleService.remove(Mockito.anyLong());

        verify(this.roleRepository, times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void tree() {
        given(this.roleRepository.findByEnabledTrue()).willReturn(Arrays.asList(Mockito.mock(Role.class), Mockito.mock(Role.class)));

        List<TreeNode> nodes = roleService.tree();
        Assertions.assertNotNull(nodes);
    }
}