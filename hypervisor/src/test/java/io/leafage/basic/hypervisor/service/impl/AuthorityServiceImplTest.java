package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.entity.Authority;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * 权限service测试
 *
 * @author liwenqiang 2021/5/11 10:10
 **/
@ExtendWith(MockitoExtension.class)
class AuthorityServiceImplTest {

    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private AuthorityServiceImpl authorityService;

    @Test
    void retrieve() {
        List<Authority> authorities = new ArrayList<>(2);
        Page<Authority> page = new PageImpl<>(authorities);
        given(this.authorityRepository.findAll(PageRequest.of(0, 2, Sort.by("id")))).willReturn(page);
        Page<AuthorityVO> voPage = authorityService.retrieve(0, 2, "id");
        Assertions.assertNotNull(voPage);
    }

    @Test
    void create() {
        given(this.authorityRepository.save(Mockito.any(Authority.class))).willReturn(Mockito.mock(Authority.class));
        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType("M");
        authorityDTO.setPath("/test");
        AuthorityVO authorityVO = authorityService.create(authorityDTO);
        verify(this.authorityRepository, times(1)).save(Mockito.any(Authority.class));
        Assertions.assertNotNull(authorityVO);
    }

    @Test
    void remove() {
        given(this.authorityRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Authority.class));
        authorityService.remove("2119JD09");
        verify(this.authorityRepository, times(1)).deleteById(Mockito.anyLong());
    }
}