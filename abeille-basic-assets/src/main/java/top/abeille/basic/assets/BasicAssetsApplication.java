/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liwenqiang
 */
@SpringBootApplication(scanBasePackages = {"top.abeille.basic.assets", "top.abeille.common"})
public class BasicAssetsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicAssetsApplication.class, args);
    }

}

