package top.abeille.basic.data.vo;

import top.abeille.basic.BasicVOTest;
import top.abeille.common.vo.UserInfoVO;

/**
 * UserInfoVO Test
 *
 * @author liwenqiang 2019/1/29 17:33
 **/
public class UserInfoVOTest extends BasicVOTest<UserInfoVO> {

    @Override
    protected UserInfoVO getT() {
        return new UserInfoVO();
    }

}