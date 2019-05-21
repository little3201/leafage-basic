/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"top.abeille.basic.profile", "top.abeille.common"})
@EnableJpaRepositories(basePackages = {"top.abeille.basic.profile", "top.abeille.common"})
@SpringBootApplication(scanBasePackages = {"top.abeille.basic.profile", "top.abeille.common"})
public class BasicProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicProfileApplication.class, args);
    }

}

