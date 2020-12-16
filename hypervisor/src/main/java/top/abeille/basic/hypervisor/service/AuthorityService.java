/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import org.springframework.data.domain.Page;
import top.abeille.basic.hypervisor.dto.SourceDTO;
import top.abeille.basic.hypervisor.entity.Authority;
import top.abeille.basic.hypervisor.vo.SourceVO;
import top.abeille.common.basic.BasicService;

import java.util.List;

/**
 * 权限资源信息Service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface AuthorityService extends BasicService<SourceDTO, SourceVO> {

    List<Authority> findByIdIn(List<Long> idList);

    Page<SourceVO> retrieves(int page, int size);
}
