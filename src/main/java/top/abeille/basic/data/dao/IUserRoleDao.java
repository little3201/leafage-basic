package top.abeille.basic.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.data.model.UserRoleModel;

/**
 * 用户角色Dao
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
public interface IUserRoleDao extends JpaRepository<UserRoleModel, Long> {
}
