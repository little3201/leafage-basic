/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import top.abeille.basic.hypervisor.entity.RoleInfo;

/**
 * 角色信息dao接口
 *
 * @author liwenqiang 2018/9/26 11:06
 **/
public interface RoleInfoRepository extends ReactiveMongoRepository<RoleInfo, Long> {
}
