/*
 *  Copyright 2018-2022 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

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