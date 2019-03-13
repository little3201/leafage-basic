package top.abeille.basic.profile.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.profile.data.model.AccountInfoModel;

/**
 * 账户信息dao
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
public interface AccountInfoDao extends JpaRepository<AccountInfoModel, Long> {
}
