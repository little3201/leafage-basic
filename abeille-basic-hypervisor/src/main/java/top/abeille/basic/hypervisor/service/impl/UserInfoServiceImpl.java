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
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
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
public class UserInfoServiceImpl extends AbstractBasicService implements UserInfoService {

    /**
     * 开启日志
     */
    private static final Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    private final UserInfoRepository userInfoRepository;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public Page<UserVO> retrieveByPage(Pageable pageable) {
        ExampleMatcher exampleMatcher = this.appendConditions();
        UserInfo userInfo = this.appendParams(new UserInfo());
        Page<UserInfo> infoPage = userInfoRepository.findAll(Example.of(userInfo, exampleMatcher), pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(this::convertOuter);
    }

    @Override
    public UserVO create(UserDTO userDTO) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(userDTO, info);
        info.setBusinessId(PrefixEnum.US + this.generateId());
        info.setModifyTime(LocalDateTime.now());
        userInfoRepository.save(info);
        return this.convertOuter(info);
    }

    @Override
    public UserVO modify(String businessId, UserDTO userDTO) {
        Optional<UserInfo> optional = this.fetchInfo(businessId);
        if (optional.isEmpty()) {
            log.info("修改对象未找到");
            return null;
        }
        UserInfo info = optional.get();
        info.setModifier(0L);
        userInfoRepository.save(info);
        return this.convertOuter(info);
    }

    @Override
    public void removeById(String businessId) {
        Optional<UserInfo> optional = this.fetchInfo(businessId);
        if (optional.isPresent()) {
            UserInfo info = optional.get();
            userInfoRepository.deleteById(info.getId());
        }
    }

    @Override
    public void removeInBatch(List<UserDTO> dtoList) {
    }

    @Override
    public UserVO fetchByBusinessId(String businessId) {
        UserInfo info = new UserInfo();
        info.setBusinessId(businessId);
        this.appendParams(info);
        Optional<UserInfo> optional = userInfoRepository.findOne(Example.of(info));
        if (optional.isEmpty()) {
            return null;
        }
        return this.convertOuter(info);
    }

    /**
     * 根据业务ID查信息
     *
     * @param businessId 业务ID
     * @return 数据库对象信息
     */
    private Optional<UserInfo> fetchInfo(String businessId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setBusinessId(businessId);
        this.appendParams(userInfo);
        return userInfoRepository.findOne(Example.of(userInfo));
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

    /**
     * 转换为输出对象
     *
     * @return ExampleMatcher
     */
    private UserVO convertOuter(UserInfo info) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(info, userVO);
        return userVO;
    }
}
