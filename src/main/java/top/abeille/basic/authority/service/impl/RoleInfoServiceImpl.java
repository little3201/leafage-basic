package top.abeille.basic.authority.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.dao.IRoleInfoDao;
import top.abeille.basic.authority.model.RoleInfoModel;
import top.abeille.basic.authority.service.IRoleInfoService;

import java.util.List;
import java.util.Optional;

/**
 * 角色信息service 实现
 *
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleInfoServiceImpl implements IRoleInfoService {

    private final IRoleInfoDao roleInfoDao;

    @Autowired
    public RoleInfoServiceImpl(IRoleInfoDao roleInfoDao) {
        this.roleInfoDao = roleInfoDao;
    }

    @Override
    public Page<RoleInfoModel> findAllByPage(Pageable pageable) {
        return roleInfoDao.findAll(pageable);
    }

    @Override
    public List<RoleInfoModel> findAllByExample(RoleInfoModel roleInfoModel, ExampleMatcher exampleMatcher) {
        // 创建查询模板实例
        Example<RoleInfoModel> example = Example.of(roleInfoModel, exampleMatcher);
        return roleInfoDao.findAll(example);
    }

    @Override
    public RoleInfoModel getById(Long id) {
        Optional<RoleInfoModel> optional = roleInfoDao.findById(id);
        return optional.orElse(null);
    }

    @Override
    public RoleInfoModel getByExample(RoleInfoModel roleInfo) {
        Optional<RoleInfoModel> optional = roleInfoDao.findOne(Example.of(roleInfo));
        //需要对结果做判断，查询结果为null时会报NoSuchElementException
        return optional.orElse(null);
    }

}
