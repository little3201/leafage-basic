/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
import top.leafage.common.TreeNode;
import top.leafage.common.servlet.ServletBasicService;

import java.util.List;

/**
 * privilege service.
 *
 * @author wq li
 */
public interface PrivilegeService extends ServletBasicService<PrivilegeDTO, PrivilegeVO> {

    /**
     * 获取树结构数据
     *
     * @return 树结构数据集
     */
    List<TreeNode> tree();

}
