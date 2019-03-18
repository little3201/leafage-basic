package top.abeille.basic.authority.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.server.dao.PermInfoDao;
import top.abeille.basic.authority.server.model.PermInfoModel;
import top.abeille.basic.authority.server.service.PermInfoService;

import java.util.List;
import java.util.Optional;

/**
 * 权限资源信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class PermInfoServiceImpl implements PermInfoService {

    private final PermInfoDao permInfoDao;

    @Autowired
    public PermInfoServiceImpl(PermInfoDao permInfoDao) {
        this.permInfoDao = permInfoDao;
    }

    @Override
    public Page<PermInfoModel> findAllByPage(Integer curPage, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(curPage, pageSize, sort);
        return permInfoDao.findAll(pageable);
    }

    @Override
    public List<PermInfoModel> findAll(Sort sort) {
        return permInfoDao.findAll(sort);
    }

    @Override
    public PermInfoModel save(PermInfoModel entity) {
        return permInfoDao.save(entity);
    }

    @Override
    public List<PermInfoModel> saveAll(List<PermInfoModel> entities) {
        return permInfoDao.saveAll(entities);
    }

    @Override
    public void removeById(Long id) {
        permInfoDao.deleteById(id);
    }

    @Override
    public void removeInBatch(List<PermInfoModel> entities) {
        permInfoDao.deleteInBatch(entities);
    }

    @Override
    public PermInfoModel getByExample(PermInfoModel permInfo) {
        Optional<PermInfoModel> optional = permInfoDao.findOne(Example.of(permInfo));
        return optional.orElse(null);
    }
}
