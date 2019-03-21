package top.abeille.basic.authority;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"top.abeille"})
@SpringBootApplication
public class BasicAuthorityApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicAuthorityApplication.class, args);
    }

}

