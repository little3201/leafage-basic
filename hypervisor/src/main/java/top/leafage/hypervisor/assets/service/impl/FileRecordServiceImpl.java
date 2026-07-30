/*
 * Copyright(c) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.hypervisor.assets.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.leafage.common.logging.annotation.OperationLog;
import top.leafage.hypervisor.assets.domain.FileRecord;
import top.leafage.hypervisor.assets.domain.dto.FileRecordDTO;
import top.leafage.hypervisor.assets.domain.vo.FileRecordVO;
import top.leafage.hypervisor.assets.domain.vo.FileStatisticsVO;
import top.leafage.hypervisor.assets.repository.FileRecordRepository;
import top.leafage.hypervisor.assets.service.FileRecordService;

import java.io.IOException;
import java.util.*;

import static top.leafage.hypervisor.constants.GlobalConstant.ID_MUST_NOT_BE_NULL;

/**
 * file service impl.
 *
 * @author wq li
 */
@OperationLog("files")
@Service
public class FileRecordServiceImpl implements FileRecordService {

    private static final Logger logger = LoggerFactory.getLogger(FileRecordServiceImpl.class);

    private static final String IMAGE = "Image";
    private static final String VIDEO = "Video";
    private static final String DOCUMENT = "Document";
    private static final String OTHER = "Other";
    private static final List<String> CATEGORY = List.of(IMAGE, VIDEO, DOCUMENT, OTHER);

    private final FileRecordRepository fileRecordRepository;

    /**
     * Constructor for FileRecordRepository.
     *
     * @param fileRecordRepository a {@link FileRecordRepository} object
     */
    public FileRecordServiceImpl(FileRecordRepository fileRecordRepository) {
        this.fileRecordRepository = fileRecordRepository;
    }

    @Override
    public Page<FileRecordVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<FileRecord> spec = (root, _, cb) -> {
            Optional<Predicate> predicate = buildPredicate(filters, cb, root);
            Predicate basePredicate = predicate.orElse(cb.conjunction());
            if (StringUtils.hasText(filters) && filters.contains("superiorId")) {
                return basePredicate;
            } else {
                return cb.and(basePredicate, cb.isNull(root.get("superiorId")));
            }
        };

        return fileRecordRepository.findAllBy(spec, pageable)
                .map(FileRecordVO::from);
    }

    @Override
    public FileRecordVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return fileRecordRepository.findById(id)
                .map(FileRecordVO::from)
                .orElseThrow(() -> new EntityNotFoundException("file record not found: " + id));
    }

    @Override
    public FileRecordVO create(FileRecordDTO dto) {
        if (fileRecordRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }

        FileRecord entity = fileRecordRepository.save(FileRecordDTO.toEntity(dto));
        return FileRecordVO.from(entity);
    }

    @Transactional
    @Override
    public FileRecordVO upload(MultipartFile file, Long superiorId) {
        FileRecord record = new FileRecord();
        record.setSuperiorId(superiorId);
        record.setName(file.getName());
        // get extension
        if (file.getOriginalFilename() != null) {
            String originalFilename = file.getOriginalFilename();
            int lastDot = originalFilename.lastIndexOf('.');
            if (lastDot > 0) {
                record.setExtension(originalFilename.substring(lastDot + 1));
            }
        }
        try {
            record.setPath(file.getResource().getFilePath().toString());
        } catch (IOException e) {
            logger.error("Get file path error: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        record.setContentType(Objects.requireNonNull(file.getContentType()));
        record.setSize(file.getSize());
        record.setDirectory(false);
        FileRecord entity = fileRecordRepository.save(record);
        return FileRecordVO.from(entity);
    }

    @Override
    public List<FileStatisticsVO> statistics() {
        List<FileRecord> fileRecords = fileRecordRepository.findAllBy();
        if (CollectionUtils.isEmpty(fileRecords)) {
            return Collections.emptyList();
        }

        Map<String, FileStatisticsVO> result = new HashMap<>();
        for (FileRecord file : fileRecords) {
            if (file.isDirectory()) {
                continue;
            }

            String category = category(file);
            result.compute(
                    category,
                    (key, value) -> value == null
                            ? new FileStatisticsVO(key, 1, file.getSize())
                            : new FileStatisticsVO(
                            key,
                            value.count() + 1,
                            value.size() + file.getSize()
                    )
            );
        }
        return CATEGORY.stream().map(category -> result.getOrDefault(
                        category,
                        new FileStatisticsVO(category, 0, 0)
                ))
                .toList();
    }

    @Transactional
    @Override
    public boolean enable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!fileRecordRepository.existsById(id)) {
            throw new EntityNotFoundException("file not found: " + id);
        }
        return fileRecordRepository.enableById(id) > 0;
    }

    @Transactional
    @Override
    public boolean disable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!fileRecordRepository.existsById(id)) {
            throw new EntityNotFoundException("file not found: " + id);
        }
        return fileRecordRepository.disableById(id) > 0;
    }

    @Transactional
    @Override
    public void remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!fileRecordRepository.existsById(id)) {
            throw new EntityNotFoundException("file record not found: " + id);
        }
        fileRecordRepository.deleteById(id);
    }

    private String category(FileRecord fileRecord) {
        String mime = fileRecord.getContentType();

        if (mime != null) {
            if (mime.startsWith("image/")) {
                return "Image";
            }
            if (mime.startsWith("video/")) {
                return "Video";
            }
            if (mime.startsWith("text/")
                    || mime.equals("application/pdf")
                    || mime.contains("word")
                    || mime.contains("excel")) {
                return "Document";
            }
        }

        return "Other";
    }
}
