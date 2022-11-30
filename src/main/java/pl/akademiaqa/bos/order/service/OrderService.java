package pl.akademiaqa.bookstore.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.akademiaqa.bookstore.books.db.BookJpaRepository;
import pl.akademiaqa.bookstore.books.domain.Book;
import pl.akademiaqa.bookstore.order.api.payload.CreateOrderPayload;
import pl.akademiaqa.bookstore.order.api.payload.UpdateStatusPayload;
import pl.akademiaqa.bookstore.order.api.response.CreateOrderResponse;
import pl.akademiaqa.bookstore.order.domain.*;
import pl.akademiaqa.bookstore.order.service.port.IOrderService;
import pl.akademiaqa.bookstore.order.db.OrderJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderJpaRepository repository;
    private final BookJpaRepository bookRepository;


    @Override
    @Transactional
    public List<RichOrder> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toRichOrder)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RichOrder> findById(Long id) {
        return repository.findById(id).map(this::toRichOrder);
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        Optional<Order> order = repository.findById(id);
        if (order.isPresent()) {
            repository.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderPayload payload) {
        Order savedOrder = repository.save(payload.toOrder());
        if (savedOrder == null) {
            return CreateOrderResponse.failure("Can not create an order");
        }
        return CreateOrderResponse.success(savedOrder.getId());
    }

    @Override
    public ResponseEntity<RichOrder> updateOrderStatus(Long id, UpdateStatusPayload payload) {
        OrderStatus status = OrderStatus
                .parseString(payload.getStatus())
                .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + payload.getStatus()));

        return repository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    repository.save(order);
                    Optional<RichOrder> richOrder = repository.findById(id).map(this::toRichOrder);
                    return ResponseEntity.ok(richOrder.get());
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private RichOrder toRichOrder(Order order) {
        List<RichOrderItem> richItems = toRichItems(order.getItems());
        return new RichOrder(
                order.getId(),
                order.getStatus(),
                richItems,
                order.getRecipient(),
                order.getCreatedAt()
        );
    }

    private List<RichOrderItem> toRichItems(List<OrderItem> items) {
        return items.stream()
                .map(item -> {
                    Book book = bookRepository
                            .findById(item.getBookId())
                            .orElseThrow(() -> new IllegalStateException("Unable to find book with ID: " + item.getBookId()));
                    return new RichOrderItem(book, item.getQuantity());
                })
                .collect(Collectors.toList());
    }
}
