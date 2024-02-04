package io.leafage.basic.assets.config;

import io.leafage.basic.assets.audit.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * jpa config.
 *
 * @author wq li  2020-12-20 9:54
 */
@Configuration
@EnableJpaAuditing
public class AuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
