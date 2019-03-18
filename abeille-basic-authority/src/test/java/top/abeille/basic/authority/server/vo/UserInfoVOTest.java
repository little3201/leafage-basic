package top.abeille.basic.authority.server.vo;

import top.abeille.basic.authority.BasicVOTest;
import top.abeille.common.vo.UserInfoVO;

/**
 * UserInfoVO Test
 *
 * @author liwenqiang 2019/1/29 17:33
 **/
public class UserInfoVOTest extends BasicVOTest<UserInfoVO> {

    @Override
    protected UserInfoVO setVO() {
        return new UserInfoVO();
    }

}