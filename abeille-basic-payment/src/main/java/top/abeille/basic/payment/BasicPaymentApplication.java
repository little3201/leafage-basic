/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liwenqiang
 */
@SpringBootApplication(scanBasePackages = {"top.abeille.basic.payment", "top.abeille.common"})
public class BasicPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicPaymentApplication.class, args);
    }

}

