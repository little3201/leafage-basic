package top.abeille.basic.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import top.abeille.basic.data.dao.IRolePermDao;
import top.abeille.basic.data.model.RolePermModel;
import top.abeille.basic.data.service.RolePermService;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * 角色权限信息Service实现
 *
 * @author liwenqiang 2018/9/26 11:40
 **/
@Service
public class RolePermServiceImpl implements RolePermService {

    private final IRolePermDao rolePermDao;

    @Autowired
    public RolePermServiceImpl(IRolePermDao rolePermDao) {
        this.rolePermDao = rolePermDao;
    }

    @Override
    public List<RolePermModel> findAllByExample(RolePermModel rolePermModel, ExampleMatcher exampleMatcher) {
        // 创建查询模板实例
        Example<RolePermModel> example = Example.of(rolePermModel, exampleMatcher);
        return rolePermDao.findAll(example);
    }

    @Override
    public RolePermModel save(RolePermModel entity) {
        return rolePermDao.save(entity);
    }

    @Override
    public List<RolePermModel> saveAll(List<RolePermModel> entities) {
        return rolePermDao.saveAll(entities);
    }

    @Override
    public void removeById(Long id) {
        rolePermDao.deleteById(id);
    }

    @Override
    public void removeInBatch(List<RolePermModel> entities) {
        rolePermDao.deleteInBatch(entities);
    }

    @Override
    public List<RolePermModel> findAllByPermId(Long permId) {
        RolePermModel rolePerm = new RolePermModel();
        rolePerm.setPermId(permId);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher(String.valueOf(permId), exact());
        return this.findAllByExample(rolePerm, exampleMatcher);
    }

}
