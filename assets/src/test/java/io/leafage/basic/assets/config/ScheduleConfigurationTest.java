package io.leafage.basic.assets.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@TestConfiguration
class ScheduleConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ScheduleConfiguration.class));

    @Test
    void configTest() {
        this.contextRunner.run((context) -> {
            assertThat(context).hasSingleBean(ScheduleConfiguration.class);
            assertThat(context).getBeanNames(ScheduleConfiguration.class).hasSize(1);
        });
    }
}