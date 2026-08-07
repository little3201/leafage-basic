/*
 * Copyright(c) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.hypervisor.schedulers.schedule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import top.leafage.hypervisor.logging.service.OperationLogService;
import top.leafage.hypervisor.schedulers.service.SchedulerLogService;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoggingScheduleTest {

    @Mock
    private OperationLogService operationLogService;

    @Mock
    private SchedulerLogService schedulerLogService;

    @InjectMocks
    private LoggingSchedule loggingSchedule;

    @Test
    void clear() {
        loggingSchedule.clear();

        verify(operationLogService).clear();
        verify(schedulerLogService).clear();
    }
}
