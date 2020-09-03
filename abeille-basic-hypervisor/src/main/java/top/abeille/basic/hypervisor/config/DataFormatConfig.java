/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.hypervisor.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@JsonComponent
@ConditionalOnProperty(name = "spring.jackson.date-format")
public class DataFormatConfig {

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String pattern;

    public Jackson2ObjectMapperBuilderCustomizer objectMapperBuilderCustomizer() {
        return customizer -> {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat(pattern);
            df.setTimeZone(tz);
            customizer.failOnEmptyBeans(false)
                    .failOnUnknownProperties(false)
                    .featuresToDisable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
                    .dateFormat(df);
        };
    }
}
