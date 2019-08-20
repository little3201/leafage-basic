/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author liwenqiang
 */
@SpringBootApplication(scanBasePackages = {"top.abeille.basic.hypervisor", "top.abeille.common"})
public class BasicHypervisorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicHypervisorApplication.class, args);
    }

}

