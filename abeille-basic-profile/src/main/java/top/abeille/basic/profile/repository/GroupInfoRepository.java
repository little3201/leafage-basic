/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.profile.entity.GroupInfo;

/**
 * 组织信息dao
 *
 * @author liwenqiang 2018/12/20 9:52
 **/
public interface GroupInfoRepository extends JpaRepository<GroupInfo, Long> {
}
