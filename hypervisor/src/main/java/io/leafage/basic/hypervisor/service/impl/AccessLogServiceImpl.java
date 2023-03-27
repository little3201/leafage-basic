/*
 *  Copyright 2018-2023 the original author or authors.
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
import io.leafage.basic.hypervisor.repository.AccessLogRepository;
import io.leafage.basic.hypervisor.service.AccessLogService;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * access log service impl
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public AccessLogServiceImpl(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    @Override
    public Mono<Page<AccessLogVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<AccessLogVO> voFlux = accessLogRepository.findAll(pageRequest).map(this::convertOuter);

        Mono<Long> count = accessLogRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    /**
     * 对象转换
     *
     * @param info 数据对象
     * @return 输出对象
     */
    private AccessLogVO convertOuter(AccessLog info) {
        AccessLogVO outer = new AccessLogVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
