/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import org.springframework.data.domain.Page;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.vo.GroupVO;
import top.abeille.common.basic.BasicService;

/**
 * 组织信息Service
 *
 * @author liwenqiang 2018/12/17 19:24
 **/
public interface GroupService extends BasicService<GroupDTO, GroupVO> {

    Page<GroupVO> retrieves(int page, int size);
}
