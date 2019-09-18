/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.assets.entity.AccountInfo;
import top.abeille.common.basic.BasicService;

/**
 * 账户信息Service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface AccountInfoService extends BasicService<AccountInfo> {

    Mono<AccountInfo> getByAccountId(String accountId);

    Mono<Void> removeByAccountId(String accountId);
}
