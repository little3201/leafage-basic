package top.abeille.basic.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.data.model.PermInfoModel;

/**
 * 权限资源dao
 *
 * @author liwenqiang 2018/12/17 19:37
 **/
public interface IPermInfoDao extends JpaRepository<PermInfoModel, Long> {

}
