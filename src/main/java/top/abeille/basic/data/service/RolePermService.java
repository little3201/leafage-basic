package top.abeille.basic.data.service;

import top.abeille.basic.data.model.RolePermModel;
import top.abeille.common.basic.BasicService;

import java.util.List;

/**
 * 角色权限Service接口
 *
 * @author liwenqiang 2018/9/26 11:40
 **/
public interface RolePermService extends BasicService<RolePermModel> {

    /**
     * 查询角色权限所有信息——根据权限ID
     * @param permId
     * @return
     */
    List<RolePermModel> findAllByPermId(Long permId);
}
