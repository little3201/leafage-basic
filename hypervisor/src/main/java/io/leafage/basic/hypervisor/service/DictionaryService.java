package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;
import java.util.List;

/**
 * dictionary service.
 *
 * @author liwenqiang 2022-04-06 17:38
 **/
public interface DictionaryService extends ServletBasicService<DictionaryDTO, DictionaryVO, String> {


    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询结果
     */
    Page<DictionaryVO> retrieve(int page, int size);

    /**
     * 获取子节点
     *
     * @return 数据集
     */
    List<DictionaryVO> lower(Long id);
}
