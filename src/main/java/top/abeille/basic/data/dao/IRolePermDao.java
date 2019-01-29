package top.abeille.basic.data.dao;

import top.abeille.basic.data.model.RolePermModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 角色权限dao接口
 *
 * @author liwenqiang 2018/9/26 11:29
 **/
public interface IRolePermDao extends JpaRepository<RolePermModel, Long> {
}
