/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.entity.UserInfo;
import top.abeille.basic.authority.repository.UserInfoDao;
import top.abeille.basic.authority.service.UserInfoService;

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

    private final UserInfoDao userInfoDao;

    public UserInfoServiceImpl(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    @Override
    public UserInfo getById(Long id) {
        /*使用getOne()返回的是引用，无法直接操作，会出现hibernate lazyxxx  no session 的错误
        在测试操作数据的方法(add/update)上加入@Transactional注解可以解决报错的问题
        return userInfoDao.getOne(id);*/
        Optional<UserInfo> optional = userInfoDao.findById(id);
        /*使用Optional的内部方法isPresent()判断查询结果是否为null*/
        return optional.orElse(null);
    }

    @Override
    public UserInfo getByExample(UserInfo userInfo) {
        /*Example对象可以当做查询条件处理，将查询条件得参数对应的属性进行设置即可
        可以通过ExampleMatcher.matching()方法进行进一步得处理*/
        ExampleMatcher exampleMatcher = this.appendConditions();
        this.appendParams(userInfo);
        Optional<UserInfo> optional = userInfoDao.findOne(Example.of(userInfo, exampleMatcher));
        /*需要对结果做判断，查询结果为null时会报NoSuchElementExceptiontrue*/
        return optional.orElse(null);
    }

    @Override
    public Page<UserInfo> findAllByPage(Integer pageNum, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        ExampleMatcher exampleMatcher = this.appendConditions();
        UserInfo userInfo = this.appendParams(new UserInfo());
        return userInfoDao.findAll(Example.of(userInfo, exampleMatcher), pageable);
    }

    @Override
    public UserInfo save(UserInfo entity) {
        return userInfoDao.save(entity);
    }

    @Override
    public void removeById(String userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        UserInfo example = this.getByExample(userInfo);
        if (example == null) {
            return;
        }
        userInfoDao.deleteById(example.getId());
    }

    @Override
    public void removeInBatch(List<UserInfo> entities) {
        userInfoDao.deleteInBatch(entities);
    }

    @Override
    public UserInfo getByUsername(String username) {
        return userInfoDao.getByUsername(username);
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
        userInfo.setAccountNonLocked(true);
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
