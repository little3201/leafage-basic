package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.vo.ResourceVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

public interface ResourceService extends ServletBasicService<ResourceDTO, ResourceVO, String> {

    /**
     * 分页查询
     *
     * @param page 分页
     * @param size 大小
     * @return 结果集
     */
    Page<ResourceVO> retrieve(int page, int size);
}
