package top.abeille.basic.authority.vo;

import top.abeille.common.mock.BasicVOMock;
import top.abeille.common.vo.UserInfoVO;

/**
 * UserInfoVO Test
 *
 * @author liwenqiang 2019/1/29 17:33
 **/
public class UserInfoVOTest extends BasicVOMock<UserInfoVO> {

    @Override
    protected UserInfoVO setVO() {
        return new UserInfoVO();
    }

}