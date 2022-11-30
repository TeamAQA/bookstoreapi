package pl.akademiaqa.bos.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Value
public class RichOrder {

    Long id;
    OrderStatus status;
    Set<OrderItem> items;
    Recipient recipient;
    @JsonIgnore
    LocalDateTime createdAt;

    public BigDecimal totalPrice() {
        return items.stream()
                .map(item -> item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
