package top.abeille.basic.profile.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.abeille.basic.profile.data.service.GroupInfoService;
import top.abeille.basic.profile.data.dao.GroupInfoDao;
import top.abeille.basic.profile.data.model.GroupInfoModel;

import java.util.List;
import java.util.Optional;

/**
 * 组织信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupInfoServiceImpl implements GroupInfoService {

    private final GroupInfoDao groupInfoDao;

    @Autowired
    public GroupInfoServiceImpl(GroupInfoDao groupInfoDao) {
        this.groupInfoDao = groupInfoDao;
    }

    @Override
    public GroupInfoModel getById(Long id) {
        Optional<GroupInfoModel> optional = groupInfoDao.findById(id);
        return optional.orElse(null);
    }

    @Override
    public GroupInfoModel save(GroupInfoModel entity) {
        return groupInfoDao.save(entity);
    }

    @Override
    public void removeById(Long id) {
        groupInfoDao.deleteById(id);
    }

    @Override
    public void removeInBatch(List<GroupInfoModel> entities) {
        groupInfoDao.deleteInBatch(entities);
    }
}
