package top.abeille.basic.authority.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.authority.data.model.RolePermModel;

/**
 * 角色权限dao接口
 *
 * @author liwenqiang 2018/9/26 11:29
 **/
public interface RolePermDao extends JpaRepository<RolePermModel, Long> {
}
