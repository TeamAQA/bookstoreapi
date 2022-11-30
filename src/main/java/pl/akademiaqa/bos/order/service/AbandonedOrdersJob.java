package pl.akademiaqa.bos.order.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.akademiaqa.bos.order.api.payload.UpdateStatusPayload;
import pl.akademiaqa.bos.order.db.OrderJpaRepository;
import pl.akademiaqa.bos.order.domain.Order;
import pl.akademiaqa.bos.order.domain.OrderStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class AbandonedOrdersJob {
    private final OrderJpaRepository repository;
    private final OrderService orderService;
    private final OrderProperties orderProperties;

    @Transactional
    @Scheduled(cron = "${app.orders.abandon-cron}")
    public void run() {
        Duration paymentPeriod = orderProperties.getPaymentPeriod();
        LocalDateTime olderThan = LocalDateTime.now().minus(paymentPeriod);
        List<Order> orders = repository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, olderThan);
        log.info("Found orders to be abandoned " + orders.size());
        UpdateStatusPayload updateStatusPayload = new UpdateStatusPayload();
        updateStatusPayload.setStatus("ABANDONED");
        orders.forEach(order -> orderService.updateOrderStatus(order.getId(), updateStatusPayload));
    }
}
