package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.AccessLog;
import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.repository.AccessLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.BDDMockito.given;

/**
 * record service test
 *
 * @author liwenqiang 2022/3/18 22:07
 */
@ExtendWith(MockitoExtension.class)
class AccessLogServiceImplTest {

    @Mock
    private AccessLogRepository accessLogRepository;

    @InjectMocks
    private AccessLogServiceImpl recordService;

    @Test
    void retrieve() {
        AccessLog accessLog = new AccessLog();
        accessLog.setIp("12.1.2.1");
        accessLog.setLocation("某国某城市");
        accessLog.setDescription("更新个人资料");
        given(this.accessLogRepository.findByEnabledTrue(Mockito.any(PageRequest.class))).willReturn(Flux.just(accessLog));

        given(this.accessLogRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(recordService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        AccessLog accessLog = new AccessLog();
        accessLog.setIp("12.1.2.1");
        accessLog.setLocation("某国某城市");
        accessLog.setDescription("更新个人资料");
        given(this.accessLogRepository.insert(Mockito.any(AccessLog.class))).willReturn(Mono.just(accessLog));

        AccessLogDTO accessLogDTO = new AccessLogDTO();
        accessLogDTO.setIp("12.1.2.1");
        accessLogDTO.setLocation("某国某城市");
        accessLogDTO.setDescription("更新个人资料");
        StepVerifier.create(recordService.create(accessLogDTO)).expectNextCount(1).verifyComplete();
    }
}