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
import io.leafage.basic.hypervisor.repository.AccessLogRepository;
import io.leafage.basic.hypervisor.service.AccessLogService;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Pageable pageable = PageRequest.of(page, size);
        Flux<AccessLogVO> voFlux = accessLogRepository.findBy(pageable).flatMap(this::convertOuter);

        Mono<Long> count = accessLogRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    /**
     * 对象转换
     *
     * @param accessLog 数据对象
     * @return 输出对象
     */
    private Mono<AccessLogVO> convertOuter(AccessLog accessLog) {
        return Mono.just(accessLog).map(a -> {
            AccessLogVO vo = new AccessLogVO();
            BeanUtils.copyProperties(a, vo);
            vo.setLastModifiedDate(a.getLastModifiedDate().orElse(null));
            return vo;
        });
    }
}
