package top.abeille.basic.authority;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan(basePackages = {"top.abeille.basic", "top.abeille.common"})
@EnableJpaRepositories(basePackages = {"top.abeille.basic", "top.abeille.common"})
@SpringBootApplication(scanBasePackages = {"top.abeille.basic", "top.abeille.common"})
@EnableDiscoveryClient
public class BasicAuthorityApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicAuthorityApplication.class, args);
    }

}

