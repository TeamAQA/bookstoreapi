package pl.akademiaqa.bos.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.akademiaqa.bos.jpa.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseEntity {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Set<OrderItem> items;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Recipient recipient;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;

    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime updatedAt;

    public UpdateStatusResult updateStatus(OrderStatus newStatus) {
        UpdateStatusResult result = status.updateStatus(newStatus);
        this.status = result.getNewStatus();
        return result;
    }
}
