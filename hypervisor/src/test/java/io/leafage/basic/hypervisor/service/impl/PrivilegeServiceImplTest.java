package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Privilege;
import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.repository.PrivilegeRepository;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
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
import top.leafage.common.TreeNode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * privilege service test
 *
 * @author liwenqiang 2021/5/11 10:10
 **/
@ExtendWith(MockitoExtension.class)
class PrivilegeServiceImplTest {

    @Mock
    private PrivilegeRepository privilegeRepository;

    @InjectMocks
    private PrivilegeServiceImpl privilegeService;

    @Test
    void retrieve() {
        Page<Privilege> page = new PageImpl<>(List.of(Mockito.mock(Privilege.class)));
        given(this.privilegeRepository.findAll(PageRequest.of(0, 2, Sort.by("id")))).willReturn(page);

        Page<PrivilegeVO> voPage = privilegeService.retrieve(0, 2, "id");
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void create() {
        given(this.privilegeRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Privilege.class)));

        given(this.privilegeRepository.saveAndFlush(Mockito.any(Privilege.class))).willReturn(Mockito.mock(Privilege.class));

        PrivilegeVO privilegeVO = privilegeService.create(Mockito.mock(PrivilegeDTO.class));

        verify(this.privilegeRepository, times(1)).saveAndFlush(Mockito.any(Privilege.class));
        Assertions.assertNotNull(privilegeVO);
    }

    @Test
    void modify() {
        given(this.privilegeRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Privilege.class)));

        given(this.privilegeRepository.save(Mockito.any(Privilege.class))).willReturn(Mockito.mock(Privilege.class));

        PrivilegeVO privilegeVO = privilegeService.modify(Mockito.anyLong(), Mockito.mock(PrivilegeDTO.class));

        verify(this.privilegeRepository, times(1)).save(Mockito.any(Privilege.class));
        Assertions.assertNotNull(privilegeVO);
    }

    @Test
    void remove() {
        privilegeService.remove(Mockito.anyLong());
        verify(this.privilegeRepository, times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void tree() {
        given(this.privilegeRepository.findByEnabledTrue()).willReturn(Arrays.asList(Mockito.mock(Privilege.class), Mockito.mock(Privilege.class)));

        List<TreeNode> nodes = privilegeService.tree();
        Assertions.assertNotNull(nodes);
    }

}