/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.common.basic.BasicService;
import org.springframework.data.domain.Page;

/**
 * 组织信息Service
 *
 * @author liwenqiang 2018/12/17 19:24
 **/
public interface GroupService extends BasicService<GroupDTO, GroupVO> {

    Page<GroupVO> retrieves(int page, int size);
}
