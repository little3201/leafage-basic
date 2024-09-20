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

package io.leafage.basic.assets.config;

import io.leafage.basic.assets.audit.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

/**
 * audit configuration
 *
 * @author wq li
 */
@Configuration
@EnableR2dbcAuditing
public class AuditConfiguration {

    /**
     * <p>auditorProvider.</p>
     *
     * @return a {@link org.springframework.data.domain.ReactiveAuditorAware} object
     */
    @Bean
    public ReactiveAuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
