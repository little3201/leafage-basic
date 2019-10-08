/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.assets.vo.enter.AccountEnter;
import top.abeille.basic.assets.vo.outer.AccountOuter;
import top.abeille.common.basic.BasicService;

/**
 * 账户信息Service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface AccountInfoService extends BasicService<AccountEnter, AccountOuter> {

    Mono<AccountOuter> getByAccountId(Long accountId);

    Mono<Void> removeByAccountId(Long accountId);
}
