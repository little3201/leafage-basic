package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Record;
import io.leafage.basic.assets.dto.RecordDTO;
import io.leafage.basic.assets.repository.RecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * record service test
 *
 * @author liwenqiang 2022/3/18 22:07
 */
@ExtendWith(MockitoExtension.class)
class RecordServiceImplTest {

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordServiceImpl recordService;

    @Test
    void retrieve() {
        Record record = new Record();
        record.setDate(LocalDate.now());
        record.setType("bugfix");
        record.setItems(List.of("更新依赖"));
        given(this.recordRepository.findByEnabledTrue()).willReturn(Flux.just(record));

        StepVerifier.create(recordService.retrieve()).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        given(this.recordRepository.count()).willReturn(Mono.just(2L));
        StepVerifier.create(recordService.count()).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        Record record = new Record();
        record.setDate(LocalDate.now());
        record.setType("bugfix");
        record.setItems(List.of("某国"));
        given(this.recordRepository.insert(Mockito.any(Record.class))).willReturn(Mono.just(record));

        RecordDTO recordDTO = new RecordDTO();
        recordDTO.setDate(LocalDate.now());
        StepVerifier.create(recordService.create(recordDTO)).expectNextCount(1).verifyComplete();
    }
}