package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.FileRecord;
import io.leafage.basic.assets.repository.FileRecordRepository;
import io.leafage.basic.assets.vo.FileRecordVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.mockito.BDDMockito.given;

class FileServiceImplTest {

    @Mock
    private FileRecordRepository fileRecordRepository;

    @InjectMocks
    private FileServiceImpl fileService;


    @BeforeEach
    void setUp() {
    }

    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));
        Page<FileRecord> fileRecords = new PageImpl<>(List.of(Mockito.mock(FileRecord.class)));

        Specification<FileRecord> spec = (root, query, cb) -> cb.like(root.get("name"), "%test%");

        given(this.fileRecordRepository.findAll(spec, pageable)).willReturn(fileRecords);

        Page<FileRecordVO> voPage = fileService.retrieve(0, 2, "id", true, "test");
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void upload() {
    }
}