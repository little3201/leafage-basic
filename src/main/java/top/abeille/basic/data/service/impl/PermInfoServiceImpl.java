package top.abeille.basic.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import top.abeille.basic.data.dao.IPermInfoDao;
import top.abeille.basic.data.model.PermInfoModel;
import top.abeille.basic.data.service.IPermInfoService;

import java.util.Optional;

/**
 * 权限资源信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class PermInfoServiceImpl implements IPermInfoService {

    private final IPermInfoDao permInfoDao;

    @Autowired
    public PermInfoServiceImpl(IPermInfoDao permInfoDao) {
        this.permInfoDao = permInfoDao;
    }

    @Override
    public PermInfoModel getByExample(PermInfoModel permInfo) {
        Optional<PermInfoModel> optional = permInfoDao.findOne(Example.of(permInfo));
        return optional.orElse(null);
    }
}
