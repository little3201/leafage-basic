/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.repository.RoleInfoRepository;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.vo.UserVO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    private final RoleInfoRepository roleInfoRepository;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, RoleInfoRepository roleInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.roleInfoRepository = roleInfoRepository;
    }

    @Override
    public Page<UserVO> retrieveByPage(Pageable pageable) {
        ExampleMatcher exampleMatcher = this.appendConditions();
        UserInfo userInfo = this.appendParams(new UserInfo());
        Page<UserInfo> infoPage = userInfoRepository.findAll(Example.of(userInfo, exampleMatcher), pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(info -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(info, userVO);
            return userVO;
        });
    }

    @Override
    public UserVO create(UserDTO userDTO) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(userDTO, info);
        Long userId = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        info.setUserId(userId);
        info.setEnabled(true);
        info.setModifier(0L);
        userInfoRepository.save(info);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(info, userVO);
        return userVO;
    }

    @Override
    public UserVO modify(Long userId, UserDTO userDTO) {
        UserInfo info = new UserInfo();
        info.setUserId(userId);
        Optional<UserInfo> optional = userInfoRepository.findOne(Example.of(info));
        if (!optional.isPresent()) {
            return null;
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

//    @Override
    public UserInfo loadUserByUsername(String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        appendParams(userInfo);
        Optional<UserInfo> infoOptional = userInfoRepository.findOne(Example.of(userInfo));
        if (!infoOptional.isPresent()) {
            log.info("no user with username: {} be found", username);
            return null;
        }
        return infoOptional.get();
    }

    @Override
    public UserVO fetchById(Long userId) {
        // Example对象可以当做查询条件处理，将查询条件得参数对应的属性进行设置即可, 可以通过ExampleMatcher.matching()方法进行进一步得处理
        ExampleMatcher exampleMatcher = this.appendConditions();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
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
