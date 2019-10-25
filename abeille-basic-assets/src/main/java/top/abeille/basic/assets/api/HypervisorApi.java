/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.api;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 用户api
 *
 * @author liwenqiang 2019-03-03 22:55
 **/
@FeignClient(name = "abeille-basic-authority")
public interface HypervisorApi {
}
