/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author liwenqiang
 */
@SpringBootApplication(scanBasePackages = {"top.abeille.basic.user", "top.abeille.common"})
public class BasicUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicUserApplication.class, args);
    }

}

