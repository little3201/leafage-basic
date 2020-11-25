/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;


/**
 * @author liwenqiang
 */
@SpringCloudApplication
public class HypervisorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HypervisorApplication.class, args);
    }

}

