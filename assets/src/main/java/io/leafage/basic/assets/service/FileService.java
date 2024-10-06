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
public interface FileService extends ServletBasicService<FileRecordDTO, FileRecordVO> {

    Page<FileRecordVO> retrieve(int page, int size, String sortBy, boolean descending, String name);

    /**
     * 上传
     *
     * @param file 文件
     * @return 结果
     */
    FileRecordVO upload(MultipartFile file);
}
