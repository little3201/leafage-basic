package top.abeille.basic.profile.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.profile.data.model.GroupInfoModel;

/**
 * 组织信息dao
 *
 * @author liwenqiang 2018/12/20 9:52
 **/
public interface GroupInfoDao extends JpaRepository<GroupInfoModel, Long> {
}
