/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liwenqiang
 */
@SpringBootApplication(scanBasePackages = {"top.abeille.basic.profile", "top.abeille.common"})
public class BasicProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicProfileApplication.class, args);
    }

}

