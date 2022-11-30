package pl.akademiaqa.bookstore.order.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    @Singular
    private List<OrderItem> items;
    private transient Recipient recipient;
    @Builder.Default
    private OrderStatus status = OrderStatus.NEW;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
