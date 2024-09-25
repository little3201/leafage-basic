package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.FileDTO;
import io.leafage.basic.assets.vo.FileVO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import top.leafage.common.servlet.ServletBasicService;

/**
 * file service.
 *
 * @author wq li
 */
public interface FileService extends ServletBasicService<FileDTO, FileVO> {

    /**
     * 分页查询
     *
     * @param page       页码
     * @param size       大小
     * @param sortBy     排序
     * @param descending 顺序
     * @return 查询结果
     */
    Page<FileVO> retrieve(int page, int size, String sortBy, boolean descending);

    /**
     * 上传
     *
     * @param file 文件
     * @return 结果
     */
    FileVO upload(MultipartFile file);
}
