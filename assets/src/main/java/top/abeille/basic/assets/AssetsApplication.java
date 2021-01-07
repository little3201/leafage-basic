/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author liwenqiang
 */
@EnableJpaAuditing
@SpringBootApplication
public class AssetsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetsApplication.class, args);
    }

}

