/*
 *  Copyright 2018-2024 little3201.
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

import io.leafage.basic.hypervisor.domain.AccessLog;
import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.repository.AccessLogRepository;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * message service test
 *
 * @author wq li 2022/3/3 11:25
 **/
@ExtendWith(MockitoExtension.class)
class AccessLogServiceImplTest {

    @Mock
    private AccessLogRepository accessLogRepository;

    @InjectMocks
    private AccessLogServiceImpl accessLogService;

    @Test
    void retrieve() {
        Page<AccessLog> page = new PageImpl<>(List.of(Mockito.mock(AccessLog.class)));
        given(this.accessLogRepository.findAll(PageRequest.of(0, 2))).willReturn(page);

        Page<AccessLogVO> voPage = accessLogService.retrieve(0, 2);

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void create() {
        given(this.accessLogRepository.saveAndFlush(Mockito.any(AccessLog.class))).willReturn(Mockito.mock(AccessLog.class));

        AccessLogVO accessLogVO = accessLogService.create(Mockito.mock(AccessLogDTO.class));

        verify(this.accessLogRepository, times(1)).saveAndFlush(Mockito.any(AccessLog.class));
        Assertions.assertNotNull(accessLogVO);
    }
}