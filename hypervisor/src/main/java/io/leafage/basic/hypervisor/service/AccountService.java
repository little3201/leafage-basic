/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * account service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface AccountService extends ReactiveBasicService<AccountDTO, AccountVO, String> {

    /**
     * unlock
     *
     * @param username 账号
     * @return 结果
     */
    Mono<Boolean> unlock(String username);

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 结果集
     */
    Mono<Page<AccountVO>> retrieve(int page, int size);
}
