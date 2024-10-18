package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.FileRecord;
import io.leafage.basic.assets.repository.FileRecordRepository;
import io.leafage.basic.assets.service.FileService;
import io.leafage.basic.assets.vo.FileRecordVO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * file service impl.
 *
 * @author wq li
 */
@Service
public class FileServiceImpl implements FileService {

    private final FileRecordRepository fileRecordRepository;

    public FileServiceImpl(FileRecordRepository fileRecordRepository) {
        this.fileRecordRepository = fileRecordRepository;
    }

    @Override
    public Page<FileRecordVO> retrieve(int page, int size, String sortBy, boolean descending, String name) {
        Sort sort = Sort.by(descending ? Sort.Direction.DESC : Sort.Direction.ASC,
                StringUtils.hasText(sortBy) ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<FileRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return fileRecordRepository.findAll(spec, pageable).map(this::convert);
    }

    @Override
    public FileRecordVO upload(MultipartFile file) {
        return null;
    }


    /**
     * 数据转换
     *
     * @param fileRecord 数据
     * @return FileVO 输出对象
     */
    private FileRecordVO convert(FileRecord fileRecord) {
        FileRecordVO vo = new FileRecordVO();
        BeanCopier copier = BeanCopier.create(FileRecord.class, FileRecordVO.class, false);
        copier.copy(fileRecord, vo, null);
        return vo;
    }
}
