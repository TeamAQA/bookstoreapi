package pl.akademiaqa.bookstore.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akademiaqa.bookstore.order.domain.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
