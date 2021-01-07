/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.entity.Authority;
import top.abeille.basic.hypervisor.entity.RoleAuthority;
import top.abeille.basic.hypervisor.entity.User;
import top.abeille.basic.hypervisor.entity.UserRole;
import top.abeille.basic.hypervisor.repository.RoleAuthorityRepository;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.AuthorityService;
import top.abeille.basic.hypervisor.service.RoleService;
import top.abeille.basic.hypervisor.service.UserService;
import top.abeille.basic.hypervisor.vo.UserDetailsVO;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息service实现
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserServiceImpl extends AbstractBasicService implements UserService {

    /**
     * 开启日志
     */
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * email 正则
     */
    private static final String REGEX_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    /**
     * 手机号 正则
     */
    private static final String REGEX_MOBILE = "0?(13|14|15|17|18|19)[0-9]{9}";

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final RoleService roleService;
    private final AuthorityService authorityService;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleAuthorityRepository roleAuthorityRepository, RoleService roleService, AuthorityService authorityService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.roleService = roleService;
        this.authorityService = authorityService;
    }

    @Override
    public Page<UserVO> retrieves(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public UserVO create(UserDTO userDTO) {
        User info = new User();
        BeanUtils.copyProperties(userDTO, info);
        userRepository.save(info);
        return this.convertOuter(info);
    }

    @Override
    public UserVO modify(String username, UserDTO userDTO) {
        User user = userRepository.findByUsernameAndEnabledTrue(username);
        BeanUtils.copyProperties(userDTO, user);
        userRepository.save(user);
        return this.convertOuter(user);
    }

    @Override
    public void remove(String username) {
        User info = userRepository.findByUsernameAndEnabledTrue(username);
        userRepository.deleteById(info.getId());
    }

    @Override
    public UserVO fetch(String username) {
        User user = userRepository.findByUsernameAndEnabledTrue(username);
        return this.convertOuter(user);
    }

    @Override
    public UserDetailsVO fetchDetails(String username) {
        User user = userRepository.findByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username);

        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        // 检查角色是否配置

        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findByRoleIdIn(roleIds);

        // 权限
        List<Long> sourceIds = roleAuthorities.stream().map(RoleAuthority::getSourceId).collect(Collectors.toList());
        List<Authority> authorities = authorityService.findByIdIn(sourceIds);

        Set<String> codes = authorities.stream().map(Authority::getCode).collect(Collectors.toSet());

        UserDetailsVO userDetailsVO = new UserDetailsVO();
        BeanUtils.copyProperties(user, userDetailsVO);
        userDetailsVO.setAuthorities(codes);
        return userDetailsVO;
    }

    /**
     * 转换为输出对象
     *
     * @return ExampleMatcher
     */
    private UserVO convertOuter(User info) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(info, userVO);
        return userVO;
    }
}
