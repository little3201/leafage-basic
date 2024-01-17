package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.MessageDTO;
import io.leafage.basic.hypervisor.vo.MessageVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

/**
 * notification service.
 *
 * @author liwenqiang 2022/1/29 17:34
 **/
public interface MessageService extends ServletBasicService<MessageDTO, MessageVO, String> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询结果
     */
    Page<MessageVO> retrieve(int page, int size);
}
