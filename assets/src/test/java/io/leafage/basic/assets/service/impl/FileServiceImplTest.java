package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.FileRecord;
import io.leafage.basic.assets.repository.FileRecordRepository;
import io.leafage.basic.assets.vo.FileRecordVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * file record service test
 *
 * @author wq li
 **/
@ExtendWith(MockitoExtension.class)
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
        Page<FileRecord> page = new PageImpl<>(List.of(Mockito.mock(FileRecord.class)));

        given(this.fileRecordRepository.findAll(ArgumentMatchers.<Specification<FileRecord>>any(), Mockito.any(Pageable.class))).willReturn(page);

        Page<FileRecordVO> voPage = fileService.retrieve(0, 2, "id", true, "test");
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void upload() {
    }
}