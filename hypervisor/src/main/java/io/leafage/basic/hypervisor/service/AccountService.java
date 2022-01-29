/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

/**
 * account service.
 *
 * @author liwenqiang 2022/1/26 15:20
 **/
public interface AccountService extends ServletBasicService<AccountDTO, AccountVO, String> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询结果
     */
    Page<AccountVO> retrieve(int page, int size);

}
