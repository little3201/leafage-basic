package top.abeille.basic.data.dao;

import top.abeille.basic.data.model.UserInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户信息(userInfo)dao接口
 *
 * @author liwenqiang 2018/7/27 17:50
 **/
public interface IUserInfoDao extends JpaRepository<UserInfoModel, Long> {

}
