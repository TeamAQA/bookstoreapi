package pl.akademiaqa.bookstore.order.domain;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class RichOrder {

    Long id;
    OrderStatus status;
    List<RichOrderItem> items;
    Recipient recipient;
    LocalDateTime createdAt;

    public BigDecimal totalPrice() {
        return items.stream()
                .map(item -> item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
