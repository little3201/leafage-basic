package top.abeille.basic.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.data.model.UserInfoModel;

/**
 * 用户信息(userInfo)dao接口
 *
 * @author liwenqiang 2018/7/27 17:50
 **/
public interface IUserInfoDao extends JpaRepository<UserInfoModel, Long> {
}
