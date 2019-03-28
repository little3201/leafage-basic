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
        Mockito.when(accountInfoDao.getOne(Mockito.anyLong())).thenReturn(Mockito.any(AccountInfoModel.class));
        AccountInfoModel account = accountInfoService.getById(0L);
        Assert.assertThat(account, Matchers.notNullValue());
    }
}