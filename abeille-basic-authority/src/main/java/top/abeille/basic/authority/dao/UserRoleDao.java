package top.abeille.basic.authority.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.authority.model.UserRoleModel;

/**
 * 用户角色Dao
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
public interface UserRoleDao extends JpaRepository<UserRoleModel, Long> {
}
