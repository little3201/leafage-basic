package top.abeille.basic.ServiceTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.abeille.basic.BasicApplicationTests;
import top.abeille.basic.authority.dao.IUserInfoDao;
import top.abeille.basic.authority.model.UserInfoModel;
import top.abeille.basic.authority.service.impl.UserInfoServiceImpl;

/**
 * java类描述
 *
 * @author liwenqiang 2018/12/30 14:02
 **/
public class UserInfoServiceTest extends BasicApplicationTests {

    @Mock
    IUserInfoDao userInfoDao;

    @InjectMocks
    UserInfoServiceImpl userInfoService;

    /**
     * 测试修改用户信息
     * 如果使用jpa的getOne(),必须加@Transactional，否则会曝出 hibernate lazyXXXXX - no session
     */
    @Test
    public void updateUserInfo() {
        UserInfoModel user = new UserInfoModel();
        user.setId(0L);
        Mockito.when(userInfoDao.findById(0L)).thenReturn(java.util.Optional.of(user));
        UserInfoModel userInfo = userInfoService.getById(0L);
        String pwd = new BCryptPasswordEncoder().encode("abeille");
        userInfo.setId(0L);
        Mockito.when(userInfoDao.save(userInfo)).thenReturn(userInfo);
        UserInfoModel updateResult = userInfoDao.save(userInfo);
        Assert.assertThat(updateResult.getId(), Matchers.equalTo(0L));
    }
}
