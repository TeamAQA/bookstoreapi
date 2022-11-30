package pl.akademiaqa.bos.order.service;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@Value
@ConstructorBinding
@ConfigurationProperties("app.orders")
public class OrderProperties {
    String abandonCron;
    Duration paymentPeriod;
}
