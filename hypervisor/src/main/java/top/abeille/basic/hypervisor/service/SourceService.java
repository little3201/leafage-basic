/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import top.abeille.basic.hypervisor.dto.SourceDTO;
import top.abeille.basic.hypervisor.entity.SourceInfo;
import top.abeille.basic.hypervisor.vo.SourceVO;
import top.abeille.common.basic.BasicService;

/**
 * 权限资源信息Service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface SourceService extends BasicService<SourceDTO, SourceVO> {

    SourceInfo findById(long id);
}
