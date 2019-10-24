/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.entity.RoleInfo;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.entity.UserRole;
import top.abeille.basic.hypervisor.repository.RoleInfoRepository;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.vo.UserDetailsVO;
import top.abeille.basic.hypervisor.vo.UserVO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * 用户信息service实现
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserInfoServiceImpl implements UserInfoService {

    /**
     * 开启日志
     */
    private static final Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    private final UserInfoRepository userInfoRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleInfoRepository roleInfoRepository;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, UserRoleRepository userRoleRepository, RoleInfoRepository roleInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleInfoRepository = roleInfoRepository;
    }

    @Override
    public Page<UserVO> fetchAllByPage(Pageable pageable) {
        ExampleMatcher exampleMatcher = this.appendConditions();
        UserInfo userInfo = this.appendParams(new UserInfo());
        Page<UserInfo> infoPage = userInfoRepository.findAll(Example.of(userInfo, exampleMatcher), pageable);
        List<UserInfo> infoList = infoPage.getContent();
        if (CollectionUtils.isEmpty(infoList)) {
            return new PageImpl<>(Collections.emptyList());
        }
        //参数转换为出参结果
        List<UserVO> voList = new ArrayList<>(infoList.size());
        for (UserInfo info : infoList) {
            UserVO articleVO = new UserVO();
            BeanUtils.copyProperties(info, articleVO);
            voList.add(articleVO);
        }
        Page<UserVO> voPage = new PageImpl<>(voList);
        BeanUtils.copyProperties(infoPage, voPage);
        return voPage;
    }

    @Override
    public UserVO save(UserDTO userDTO) {
        UserInfo info = new UserInfo();
        info.setUserId(userDTO.getUserId());
        Optional<UserInfo> userInfo = userInfoRepository.findOne(Example.of(info));
        BeanUtils.copyProperties(userDTO, info);
        if (userInfo.isPresent()) {
            info.setId(userInfo.get().getId());
        } else {
            Long userId = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
            info.setUserId(userId);
            info.setEnabled(true);
        }
        info.setModifier(0L);
        userInfoRepository.save(info);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(info, userVO);
        return userVO;
    }

    @Override
    public void removeById(Long id) {
        userInfoRepository.deleteById(id);
    }

    @Override
    public void removeInBatch(List<UserDTO> dtoList) {
    }

    @Override
    public UserDetailsVO loadUserByUsername(String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        appendParams(userInfo);
        Optional<UserInfo> infoOptional = userInfoRepository.findOne(Example.of(userInfo));
        if (!infoOptional.isPresent()) {
            log.info("no user with username: {} be found", username);
            return null;
        }
        UserDetailsVO userDetailsVO = new UserDetailsVO();
        BeanUtils.copyProperties(infoOptional.get(), userDetailsVO);
        UserRole userRole = new UserRole();
        userRole.setUserId(infoOptional.get().getId());
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher(String.valueOf(infoOptional.get().getId()), exact());
        List<UserRole> userRoles = userRoleRepository.findAll(Example.of(userRole, exampleMatcher));
        if (CollectionUtils.isEmpty(userRoles)) {
            log.info("the user with username: {} was unauthorized ", username);
            return null;
        }
        Set<String> authorities = new HashSet<>();
        userRoles.forEach(userRoleInfo -> {
            RoleInfo roleVO = roleInfoRepository.getOne(userRoleInfo.getRoleId());
            if (StringUtils.isNotBlank(roleVO.getRoleId())) {
                authorities.add(roleVO.getRoleId());
            }
        });
        userDetailsVO.setAuthorities(authorities);
        return userDetailsVO;
    }

    @Override
    public UserVO queryById(Long userId) {
        // Example对象可以当做查询条件处理，将查询条件得参数对应的属性进行设置即可, 可以通过ExampleMatcher.matching()方法进行进一步得处理
        ExampleMatcher exampleMatcher = this.appendConditions();
        UserInfo userInfo = new UserInfo();
        this.appendParams(userInfo);
        Optional<UserInfo> optional = userInfoRepository.findOne(Example.of(userInfo, exampleMatcher));
        if (!optional.isPresent()) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(optional.get(), userVO);
        return userVO;
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param userInfo 用户信息
     * @return UserInfo
     */
    private UserInfo appendParams(UserInfo userInfo) {
        userInfo.setEnabled(true);
        userInfo.setAccountNonExpired(true);
        userInfo.setCredentialsNonExpired(true);
        return userInfo;
    }

    /**
     * 设置必要参数匹配条件
     *
     * @return ExampleMatcher
     */
    private ExampleMatcher appendConditions() {
        String[] fields = new String[]{"is_enabled", "is_credentials_non_expired", "is_account_non_locked", "is_account_non_expired"};
        ExampleMatcher matcher = ExampleMatcher.matching();
        for (String param : fields) {
            matcher.withMatcher(param, exact());
        }
        return matcher;
    }
}
