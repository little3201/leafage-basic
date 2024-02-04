package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.vo.RegionVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

import java.util.List;

/**
 * region service.
 *
 * @author wq li 2021/11/27 14:18
 **/
public interface RegionService extends ServletBasicService<RegionDTO, RegionVO> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询结果
     */
    Page<RegionVO> retrieve(int page, int size);

    /**
     * 获取子节点
     *
     * @return 数据集
     */
    List<RegionVO> lower(Long id);
}
