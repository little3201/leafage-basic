package top.abeille.basic.authority.service;

import top.abeille.basic.authority.model.RolePermModel;
import top.abeille.basic.common.service.IBasicService;

import java.util.List;

/**
 * 角色权限Service接口
 *
 * @author liwenqiang 2018/9/26 11:40
 **/
public interface IRolePermService extends IBasicService<RolePermModel> {

    /**
     * 查询角色权限所有信息——根据权限ID
     * @param permId
     * @return
     */
    List<RolePermModel> findAllByPermId(Long permId);
}
