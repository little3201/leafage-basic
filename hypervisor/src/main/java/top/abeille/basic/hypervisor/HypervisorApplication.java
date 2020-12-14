/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;


/**
 * @author liwenqiang
 */
@EnableMongoAuditing
@SpringBootApplication
public class HypervisorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HypervisorApplication.class, args);
    }

}

