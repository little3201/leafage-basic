package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.service.FileService;
import io.leafage.basic.assets.vo.FileVO;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

/**
 * file service impl.
 *
 * @author wq li
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public Page<FileVO> retrieve(int page, int size, String sortBy, boolean descending) {
        Sort sort = Sort.by(descending ? Sort.Direction.DESC : Sort.Direction.ASC,
                StringUtils.hasText(sortBy) ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return new PageImpl<>(Collections.emptyList());
    }

    @Override
    public FileVO upload(MultipartFile file) {
        return null;
    }
}
