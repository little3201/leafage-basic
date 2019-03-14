package top.abeille.basic.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BasicProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicProfileApplication.class, args);
    }

}

