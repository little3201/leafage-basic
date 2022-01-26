package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.entity.Account;
import io.leafage.basic.hypervisor.entity.AccountRole;
import io.leafage.basic.hypervisor.entity.Authority;
import io.leafage.basic.hypervisor.entity.RoleAuthority;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.AccountRoleRepository;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
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
import top.leafage.common.basic.TreeNode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private AccountRepository accountRepository;

    @Mock
    private AccountRoleRepository accountRoleRepository;

    @Mock
    private RoleAuthorityRepository roleAuthorityRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private AuthorityServiceImpl authorityService;

    @Test
    void retrieve() {
        Authority authority = new Authority();
        authority.setName("test");
        authority.setDescription("测试");
        Page<Authority> page = new PageImpl<>(List.of(authority));
        given(this.authorityRepository.findAll(PageRequest.of(0, 2, Sort.by("id")))).willReturn(page);

        Page<AuthorityVO> voPage = authorityService.retrieve(0, 2, "id");
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void create() {
        Authority superior = new Authority();
        superior.setId(1L);
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(superior);

        given(this.authorityRepository.save(Mockito.any(Authority.class))).willReturn(Mockito.mock(Authority.class));

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        authorityDTO.setPath("/test");
        authorityDTO.setSuperior("2119JD09");
        AuthorityVO authorityVO = authorityService.create(authorityDTO);

        verify(this.authorityRepository, times(1)).save(Mockito.any(Authority.class));
        Assertions.assertNotNull(authorityVO);
    }

    @Test
    void modify() {
        Authority authority = new Authority();
        authority.setId(1L);
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(authority);

        Authority superior = new Authority();
        superior.setId(1L);
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(superior);

        given(this.authorityRepository.saveAndFlush(Mockito.any(Authority.class))).willReturn(Mockito.mock(Authority.class));

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        authorityDTO.setPath("/test");
        authorityDTO.setSuperior("2119JD09");
        AuthorityVO authorityVO = authorityService.modify("2119JD09", authorityDTO);

        verify(this.authorityRepository, times(1)).saveAndFlush(Mockito.any(Authority.class));
        Assertions.assertNotNull(authorityVO);
    }

    @Test
    void remove() {
        given(this.authorityRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Authority.class));
        authorityService.remove("2119JD09");
        verify(this.authorityRepository, times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void tree() {
        Authority authority = new Authority();
        authority.setId(1L);
        authority.setCode("2119JD09");
        authority.setName("test");

        Authority child = new Authority();
        child.setId(2L);
        child.setName("sub");
        child.setCode("2119JD19");
        child.setSuperior(1L);
        given(this.authorityRepository.findByEnabledTrue()).willReturn(Arrays.asList(authority, child));

        List<TreeNode> nodes = authorityService.tree();
        Assertions.assertNotNull(nodes);
    }

    @Test
    void authorities() {
        Account account = new Account();
        account.setId(1L);
        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(account);

        AccountRole accountRole = new AccountRole();
        accountRole.setAccountId(account.getId());
        accountRole.setRoleId(2L);
        given(this.accountRoleRepository.findByUserId(Mockito.anyLong())).willReturn(Collections.singletonList(accountRole));

        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setRoleId(accountRole.getRoleId());
        roleAuthority.setAuthorityId(3L);
        given(this.roleAuthorityRepository.findByRoleId(Mockito.anyLong())).willReturn(Collections.singletonList(roleAuthority));

        Authority authority = new Authority();
        authority.setId(4L);
        authority.setCode("2119JD09");
        authority.setName("test");
        authority.setType('M');
        given(this.authorityRepository.findById(Mockito.anyLong())).willReturn(Optional.of(authority));

        List<TreeNode> nodes = authorityService.authorities("admin", 'M');
        Assertions.assertNotNull(nodes);
    }
}