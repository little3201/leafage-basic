package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Record;
import io.leafage.basic.assets.dto.RecordDTO;
import io.leafage.basic.assets.repository.RecordRepository;
import io.leafage.basic.assets.service.RecordService;
import io.leafage.basic.assets.vo.RecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * record service impl
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    public RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public Flux<RecordVO> retrieve() {
        return recordRepository.findByEnabledTrue().map(this::convert);
    }

    @Override
    public Mono<Long> count() {
        return recordRepository.count();
    }

    @Override
    public Mono<RecordVO> create(RecordDTO recordDTO) {
        Record record = new Record();
        BeanUtils.copyProperties(recordDTO, record);
        return recordRepository.insert(record).map(this::convert);
    }

    private RecordVO convert(Record record) {
        RecordVO outer = new RecordVO();
        BeanUtils.copyProperties(record, outer);
        return outer;
    }
}
