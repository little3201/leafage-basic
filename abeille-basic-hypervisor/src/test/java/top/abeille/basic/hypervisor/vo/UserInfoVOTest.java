/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */

package top.abeille.basic.hypervisor.vo;

import top.abeille.common.mock.AbstractVOMock;
import top.abeille.common.vo.UserInfoVO;

/**
 * UserInfoVO Test
 *
 * @author liwenqiang 2019/1/29 17:33
 **/
public class UserInfoVOTest extends AbstractVOMock<UserInfoVO> {

    @Override
    protected UserInfoVO setVO() {
        return new UserInfoVO();
    }

}