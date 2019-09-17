/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import top.abeille.basic.hypervisor.entity.GroupInfo;

/**
 * 组织信息dao
 *
 * @author liwenqiang 2018/12/20 9:52
 **/
public interface GroupInfoRepository extends ReactiveCrudRepository<GroupInfo, Long> {
}
