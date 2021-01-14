/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

/**
 * @author liwenqiang
 */
@EnableReactiveMongoAuditing
@SpringBootApplication
public class AssetsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetsApplication.class, args);
    }

}

