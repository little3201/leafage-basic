package top.abeille.basic.authority.service.impl;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.abeille.basic.authority.dao.UserInfoDao;
import top.abeille.basic.authority.model.UserInfoModel;
import top.abeille.common.mock.AbstractServiceMock;

import java.util.Optional;

/**
 * java类描述
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
public class UserInfoServiceImplTest extends AbstractServiceMock {

    @Mock
    private UserInfoDao userInfoDao;

    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    /**
     * 测试修改用户信息
     * 如果使用jpa的getOne(),必须加@Transactional，否则会曝出 hibernate lazyXXXXX - no session
     */
    @Test
    public void save() {
        UserInfoModel user = new UserInfoModel();
        user.setId(0L);
        user.setUserNameCn("管理员");
        String pwd = new BCryptPasswordEncoder().encode("abeille");
        user.setPassword(pwd);
        userInfoService.save(user);
        Mockito.verify(userInfoDao, Mockito.times(1)).save(user);
    }

    @Test
    public void getById() {
        UserInfoModel user = new UserInfoModel();
        user.setId(0L);
        Mockito.when(userInfoDao.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        UserInfoModel userInfo = userInfoService.getById(user.getId());
        Assert.assertThat(userInfo.getId(), Matchers.equalTo(0L));
    }

    @Test
    public void getByExample() {
        UserInfoModel user = new UserInfoModel();
        user.setId(0L);
        Mockito.when(userInfoDao.findOne(Example.of(user))).thenReturn(Optional.of(user));
        UserInfoModel userInfo = userInfoService.getByExample(user);
        Assert.assertThat(userInfo.getId(), Matchers.equalTo(0L));
    }

    @Test
    public void findAllByPage() {
        Page<UserInfoModel> page = null;
        Mockito.when(userInfoDao.findAll(Mockito.any(Pageable.class))).thenReturn(page);
    }

    @Test
    public void removeById() {
    }

    @Test
    public void removeInBatch() {
    }
}