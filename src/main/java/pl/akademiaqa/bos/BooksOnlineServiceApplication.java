package pl.akademiaqa.bos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import pl.akademiaqa.bos.order.service.OrderProperties;

@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties(OrderProperties.class)
@SpringBootApplication
public class BooksOnlineServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(BooksOnlineServiceApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }
}
