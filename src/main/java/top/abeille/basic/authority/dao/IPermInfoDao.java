package top.abeille.basic.authority.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.authority.model.PermInfoModel;

/**
 * 权限资源dao
 *
 * @author liwenqiang 2018/12/17 19:37
 **/
public interface IPermInfoDao extends JpaRepository<PermInfoModel, Long> {

}
