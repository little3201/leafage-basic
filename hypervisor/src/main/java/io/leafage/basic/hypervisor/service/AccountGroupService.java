package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.entity.AccountGroup;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.GroupVO;
import java.util.List;
import java.util.Set;

/**
 * account group service.
 *
 * @author liwenqiang 2022/1/26 15:20
 **/
public interface AccountGroupService {


    /**
     * 查询关联用户
     *
     * @param code 代码
     * @return 数据集
     */
    List<AccountVO> accounts(String code);

    /**
     * 查询关联分组
     *
     * @param username 账号
     * @return 数据集
     */
    List<GroupVO> groups(String username);

    /**
     * 保存用户-分组关系
     *
     * @param username 账号
     * @param groups   分组信息
     * @return 结果集
     */
    List<AccountGroup> relation(String username, Set<String> groups);
}
