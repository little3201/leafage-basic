package top.abeille.basic.authority.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.authority.data.model.RoleInfoModel;

/**
 * 角色信息dao接口
 *
 * @author liwenqiang 2018/9/26 11:06
 **/
public interface RoleInfoDao extends JpaRepository<RoleInfoModel, Long> {
}
