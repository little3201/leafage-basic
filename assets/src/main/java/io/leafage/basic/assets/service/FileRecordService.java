package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.FileRecordDTO;
import io.leafage.basic.assets.vo.FileRecordVO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import top.leafage.common.servlet.ServletBasicService;

/**
 * file service.
 *
 * @author wq li
 */
public interface FileRecordService extends ServletBasicService<FileRecordDTO, FileRecordVO> {

    /**
     * Retrieves a paginated list of records.
     *
     * @param page       The page number (zero-based).
     * @param size       The number of records per page.
     * @param sortBy     The field to sort by. If null, records are unsorted.
     * @param descending Whether sorting should be in descending order.
     * @return A paginated list of records.
     * @since 0.3.0
     */
    Page<FileRecordVO> retrieve(int page, int size, String sortBy, boolean descending, String name);

    /**
     * 上传
     *
     * @param file 文件
     * @return 结果
     */
    FileRecordVO upload(MultipartFile file);
}
