/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import org.springframework.data.domain.Page;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.entity.Role;
import top.abeille.basic.hypervisor.vo.RoleVO;
import top.abeille.common.basic.BasicService;

/**
 * 角色信息service 接口
 *
 * @author liwenqiang 2018/9/27 14:18
 **/
public interface RoleService extends BasicService<RoleDTO, RoleVO> {

    Page<RoleVO> retrieves(int page, int size);

    Role findById(long id);
}
