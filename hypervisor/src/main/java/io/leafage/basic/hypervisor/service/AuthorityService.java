/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.entity.Authority;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import io.leafage.common.basic.BasicService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 权限资源信息Service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface AuthorityService extends BasicService<AuthorityDTO, AuthorityVO> {

    List<Authority> findByIdIn(List<Long> idList);

    Page<AuthorityVO> retrieves(int page, int size);
}
