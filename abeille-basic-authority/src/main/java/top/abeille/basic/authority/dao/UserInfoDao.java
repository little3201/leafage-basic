package top.abeille.basic.authority.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.abeille.basic.authority.model.UserInfoModel;

/**
 * 用户信息(userInfo)dao接口
 *
 * @author liwenqiang 2018/7/27 17:50
 **/
public interface UserInfoDao extends JpaRepository<UserInfoModel, Long> {

    @Query("select username, password, is_enabled,is_credentials_non_expired,is_account_non_locked,is_account_non_expired from user_info where username = ?1")
    UserInfoModel getByUsername(String username);
}
