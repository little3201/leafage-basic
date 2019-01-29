package top.abeille.basic.data.service;

import top.abeille.basic.data.model.UserRoleModel;
import top.abeille.basic.common.service.IBasicService;

import java.util.List;

/**
 * 用户角色Service
 *
 * @author liwenqiang
 * @date 2018-12-06 22:04
 **/
public interface IUserRoleService extends IBasicService<UserRoleModel> {

    /**
     * 查询所有角色——根据用户id
     * @param userId
     * @return
     */
    List<UserRoleModel> findAllByUserId(Long userId);
}
