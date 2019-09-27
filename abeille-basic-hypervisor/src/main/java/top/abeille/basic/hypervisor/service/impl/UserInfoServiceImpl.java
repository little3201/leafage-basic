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
import top.abeille.basic.hypervisor.entity.RoleInfo;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.entity.UserRole;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.basic.hypervisor.service.RoleInfoService;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.service.UserRoleService;
import top.abeille.basic.hypervisor.vo.UserVO;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final UserRoleService userRoleService;
    private final RoleInfoService roleInfoService;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, UserRoleService userRoleService, RoleInfoService roleInfoService) {
        this.userInfoRepository = userInfoRepository;
        this.userRoleService = userRoleService;
        this.roleInfoService = roleInfoService;
    }

    @Override
    public UserInfo getByExample(UserInfo userInfo) {
        /*Example对象可以当做查询条件处理，将查询条件得参数对应的属性进行设置即可
        可以通过ExampleMatcher.matching()方法进行进一步得处理*/
        ExampleMatcher exampleMatcher = this.appendConditions();
        this.appendParams(userInfo);
        Optional<UserInfo> optional = userInfoRepository.findOne(Example.of(userInfo, exampleMatcher));
        /*需要对结果做判断，查询结果为null时会报NoSuchElementExceptiontrue*/
        return optional.orElse(null);
    }

    @Override
    public Page<UserInfo> findAllByPage(Integer pageNum, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        ExampleMatcher exampleMatcher = this.appendConditions();
        UserInfo userInfo = this.appendParams(new UserInfo());
        return userInfoRepository.findAll(Example.of(userInfo, exampleMatcher), pageable);
    }

    @Override
    public UserInfo save(UserInfo entity) {
        UserInfo example = this.getByUserId(entity.getUserId());
        if (example != null) {
            entity.setId(example.getId());
        }
        if (entity.getModifier() == null) {
            entity.setModifier(0L);
        }
        return userInfoRepository.save(entity);
    }

    @Override
    public void removeById(Long id) {
        userInfoRepository.deleteById(id);
    }

    @Override
    public void removeInBatch(List<UserInfo> entities) {
        userInfoRepository.deleteInBatch(entities);
    }

    @Override
    public UserVO loadUserByUsername(String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        UserInfo example = this.getByExample(userInfo);
        if (null == example) {
            log.info("no user with username: {} be found", username);
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(example, userVO);
        List<UserRole> userRoles = userRoleService.findAllByUserId(example.getId());
        if (CollectionUtils.isEmpty(userRoles)) {
            log.info("the user with username: {} was unauthorized ", username);
            return null;
        }
        Set<String> authorities = new HashSet<>();
        userRoles.forEach(userRole -> {
            RoleInfo roleInfo = roleInfoService.getById(userRole.getRoleId());
            if (roleInfo != null && StringUtils.isNotBlank(roleInfo.getName())) {
                authorities.add(roleInfo.getName().toUpperCase());
            }
        });
        userVO.setAuthorities(authorities);
        return userVO;
    }

    @Override
    public void removeByUserId(String userId) {
        UserInfo example = this.getByUserId(userId);
        if (example == null) {
            return;
        }
        this.removeById(example.getId());
    }

    @Override
    public UserInfo getByUserId(String userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        return this.getByExample(userInfo);
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
