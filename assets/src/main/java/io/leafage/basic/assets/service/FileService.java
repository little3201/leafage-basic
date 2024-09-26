package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.FileDTO;
import io.leafage.basic.assets.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;
import top.leafage.common.servlet.ServletBasicService;

/**
 * file service.
 *
 * @author wq li
 */
public interface FileService extends ServletBasicService<FileDTO, FileVO> {

    /**
     * 上传
     *
     * @param file 文件
     * @return 结果
     */
    FileVO upload(MultipartFile file);
}
