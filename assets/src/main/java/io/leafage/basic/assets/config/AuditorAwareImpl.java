package io.leafage.basic.assets.config;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Nonnull
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("admin");
    }
}
