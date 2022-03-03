package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.Assertions;
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
import top.leafage.common.basic.TreeNode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * role service test
 *
 * @author liwenqiang 2021/5/11 10:10
 **/
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void retrieve() {
        Role role = new Role();
        role.setName("test");
        Page<Role> page = new PageImpl<>(List.of(role));
        given(this.roleRepository.findByEnabledTrue(PageRequest.of(0, 2, Sort.by("id")))).willReturn(page);

        Page<RoleVO> voPage = roleService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Role.class));

        RoleVO roleVO = roleService.fetch("2109JJL8");

        Assertions.assertNotNull(roleVO);
    }

    @Test
    void create() {
        Role role = new Role();
        role.setId(1L);
        role.setEnabled(true);
        role.setModifier(1L);
        role.setModifyTime(LocalDateTime.now());
        role.setDescription("test");
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(role);

        given(this.roleRepository.save(Mockito.any(Role.class))).willReturn(Mockito.mock(Role.class));

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setSuperior("2109JJL8");
        RoleVO roleVO = roleService.create(roleDTO);

        verify(this.roleRepository, times(1)).save(Mockito.any(Role.class));
        Assertions.assertNotNull(roleVO);
    }

    @Test
    void modify() {
        Role role = new Role();
        role.setId(1L);
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(role);

        Role superior = new Role();
        superior.setId(2L);
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(superior);

        given(this.roleRepository.saveAndFlush(Mockito.any(Role.class))).willReturn(Mockito.mock(Role.class));

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setSuperior("2109JJL8");
        RoleVO roleVO = roleService.modify("2109JJL9", roleDTO);

        verify(this.roleRepository, times(1)).saveAndFlush(Mockito.any(Role.class));
        Assertions.assertNotNull(roleVO);
    }

    @Test
    void remove() {
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Role.class));
        roleService.remove("2119JD09");

        verify(this.roleRepository, times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void tree() {
        Role role = new Role();
        role.setId(1L);
        role.setCode("2119JD09");
        role.setName("test");

        Role child = new Role();
        child.setId(2L);
        child.setName("sub");
        child.setCode("2119JD19");
        child.setSuperior(1L);
        given(this.roleRepository.findByEnabledTrue()).willReturn(Arrays.asList(role, child));

        List<TreeNode> nodes = roleService.tree();
        Assertions.assertNotNull(nodes);
    }
}