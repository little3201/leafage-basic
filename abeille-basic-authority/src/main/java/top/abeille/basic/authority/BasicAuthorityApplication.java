/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author liwenqiang
 */
@SpringBootApplication(scanBasePackages = {"top.abeille.basic.authority", "top.abeille.common"})
public class BasicAuthorityApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicAuthorityApplication.class, args);
    }

}

