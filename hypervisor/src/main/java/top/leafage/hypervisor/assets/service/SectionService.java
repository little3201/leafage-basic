/*
 * Copyright (c) 2026.  little3201.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.hypervisor.assets.service;

import top.leafage.common.data.domain.TreeNode;
import top.leafage.common.data.jpa.JpaCrudService;
import top.leafage.hypervisor.assets.domain.dto.SectionDTO;
import top.leafage.hypervisor.assets.domain.vo.SectionVO;

import java.util.List;

/**
 * section service.
 *
 * @author wq li
 */
public interface SectionService extends JpaCrudService<SectionDTO, SectionVO> {

    /**
     * tree
     *
     * @param id the pk.
     * @return the result.
     */
    List<TreeNode<Long>> reportTree(Long id);

    /**
     * tree
     *
     * @param id the pk.
     * @return the result.
     */
    List<TreeNode<Long>> schemaTree(Long id);

    /**
     * 获取子节点
     *
     * @param id th pk.
     * @return 数据集
     */
    List<SectionVO> subset(Long id);

    /**
     * Create report section
     *
     * @param id  the pk.
     * @param dto the data of section.
     * @return the result.
     */
    SectionVO createReportSection(Long id, SectionDTO dto);

    /**
     * Create schema section
     *
     * @param id  the pk.
     * @param dto the data of section.
     * @return the result.
     */
    SectionVO createSchemaSection(Long id, SectionDTO dto);
}
