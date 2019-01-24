package top.abeille.basic.authority.dao;

import top.abeille.basic.authority.model.RoleInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 角色信息dao接口
 *
 * @author liwenqiang 2018/9/26 11:06
 **/
public interface IRoleInfoDao extends JpaRepository<RoleInfoModel, Long> {
}
