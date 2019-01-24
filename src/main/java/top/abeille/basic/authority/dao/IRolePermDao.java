package top.abeille.basic.authority.dao;

import top.abeille.basic.authority.model.RolePermModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 角色权限dao接口
 *
 * @author liwenqiang 2018/9/26 11:29
 **/
public interface IRolePermDao extends JpaRepository<RolePermModel, Long> {
}
