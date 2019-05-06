package top.abeille.basic.profile.service.impl;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import top.abeille.basic.profile.dao.AccountInfoDao;
import top.abeille.basic.profile.model.AccountInfoModel;
import top.abeille.common.mock.BasicServiceMock;

import java.util.Optional;

/**
 * description
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
public class AccountInfoServiceImplTest extends BasicServiceMock {

    @Mock
    private AccountInfoDao accountInfoDao;

    @InjectMocks
    private AccountInfoServiceImpl accountInfoService;

    @Test
    public void getById() {
        AccountInfoModel account = new AccountInfoModel();
        account.setId(0L);
        Mockito.when(accountInfoDao.findById(Mockito.anyLong())).thenReturn(Optional.of(account));
        AccountInfoModel accountInfo = accountInfoService.getById(0L);
        Assert.assertThat(accountInfo, Matchers.notNullValue());
    }
}