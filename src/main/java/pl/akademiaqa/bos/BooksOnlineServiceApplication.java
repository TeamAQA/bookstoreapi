package pl.akademiaqa.bos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.akademiaqa.bos.order.service.OrderProperties;

@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties(OrderProperties.class)
@SpringBootApplication
public class BooksOnlineServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(BooksOnlineServiceApplication.class, args);
    }
}
