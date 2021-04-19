/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * @author liwenqiang
 */
@EnableJpaAuditing
@SpringBootApplication
public class HypervisorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HypervisorApplication.class, args);
    }

}

