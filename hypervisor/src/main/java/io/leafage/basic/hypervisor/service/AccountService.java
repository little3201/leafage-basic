/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.vo.AccountVO;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * account service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface AccountService extends ReactiveBasicService<AccountDTO, AccountVO, String> {

    /**
     * unlock account
     *
     * @param username 账号
     * @return 结果
     */
    Mono<Boolean> unlock(String username);
}
