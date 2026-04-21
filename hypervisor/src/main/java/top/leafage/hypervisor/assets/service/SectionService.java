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
import top.leafage.hypervisor.assets.domain.dto.SectionFieldDTO;
import top.leafage.hypervisor.assets.domain.vo.SectionFieldVO;
import top.leafage.hypervisor.assets.domain.vo.SectionVO;

import java.util.List;

/**
 * section service.
 *
 * @author wq li
 */
public interface SectionService extends JpaCrudService<SectionDTO, SectionVO> {

    /**
     * tree.
     *
     * @param archiveId the pk of archive.
     * @return the result.
     */
    List<TreeNode<Long>> archiveTree(Long archiveId);

    /**
     * tree.
     *
     * @param reportId the pk of report.
     * @return the result.
     */
    List<TreeNode<Long>> reportTree(Long reportId);

    /**
     * tree.
     *
     * @param schemaId the pk of schema.
     * @return the result.
     */
    List<TreeNode<Long>> schemaTree(Long schemaId);

    /**
     * 获取子节点.
     *
     * @param id th pk.
     * @return 数据集
     */
    List<SectionVO> subset(Long id);

    /**
     * Create report section.
     *
     * @param reportId the pk of report.
     * @param dto      the data of section.
     * @return the result.
     */
    SectionVO createReportSection(Long reportId, SectionDTO dto);

    /**
     * Create schema section.
     *
     * @param schemaId the pk of schema.
     * @param dto      the data of section.
     * @return the result.
     */
    SectionVO createSchemaSection(Long schemaId, SectionDTO dto);

    /**
     * Create archive section.
     *
     * @param archiveId the pk of archive.
     * @param dto       the data of section.
     * @return the result.
     */
    SectionVO createArchiveSection(Long archiveId, SectionDTO dto);

    /**
     * Retrieve section fields.
     *
     * @param id the pk of section.
     * @return the result.
     */
    List<SectionFieldVO> fields(Long id);

    /**
     * Create section field.
     *
     * @param dto the data of section field.
     * @return the result.
     */
    SectionFieldVO createField(SectionFieldDTO dto);

    /**
     * Modify section field.
     *
     * @param id  the pk of section field.
     * @param dto the data of section field.
     * @return the result.
     */
    SectionFieldVO modifyField(Long id, SectionFieldDTO dto);
}
