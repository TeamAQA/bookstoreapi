package pl.akademiaqa.bos.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.akademiaqa.bos.books.db.BookJpaRepository;
import pl.akademiaqa.bos.books.domain.Book;
import pl.akademiaqa.bos.order.api.payload.CreateOrderItemPayload;
import pl.akademiaqa.bos.order.api.payload.CreateOrderPayload;
import pl.akademiaqa.bos.order.api.payload.UpdateStatusPayload;
import pl.akademiaqa.bos.order.api.response.CreateOrderResponse;
import pl.akademiaqa.bos.order.db.RecipientJpaRepository;
import pl.akademiaqa.bos.order.domain.*;
import pl.akademiaqa.bos.order.service.port.IOrderService;
import pl.akademiaqa.bos.order.db.OrderJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderJpaRepository repository;
    private final BookJpaRepository bookRepository;
    private final RecipientJpaRepository recipientRepository;


    @Override
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
    @Transactional
    public ResponseEntity deleteById(Long id) {
        Optional<Order> order = repository.findById(id);
        if (order.isPresent()) {
            repository.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderPayload payload) {
        Order savedOrder = repository.save(toOrder(payload));
        bookRepository.saveAll(reduceBooks(savedOrder));
        if (savedOrder == null) {
            return CreateOrderResponse.failure("Can not create an order");
        }
        return CreateOrderResponse.success(savedOrder.getId());
    }

    @Override
    @Transactional
    public ResponseEntity<RichOrder> updateOrderStatus(Long id, UpdateStatusPayload payload) {
        OrderStatus status = OrderStatus
                .parseString(payload.getStatus())
                .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + payload.getStatus()));

        return repository.findById(id)
                .map(order -> {
                    UpdateStatusResult updateStatusResult = order.updateStatus(status);
                    if (updateStatusResult.isRevoked()) {
                        bookRepository.saveAll(revokeBooks(order));
                    }
                    repository.save(order);
                    Optional<RichOrder> richOrder = repository.findById(id).map(this::toRichOrder);
                    return ResponseEntity.ok(richOrder.get());
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private RichOrder toRichOrder(Order order) {
        return new RichOrder(
                order.getId(),
                order.getStatus(),
                order.getItems(),
                order.getRecipient(),
                order.getCreatedAt()
        );
    }

    private Order toOrder(CreateOrderPayload payload) {
        Set<OrderItem> orderItems = payload.getItems()
                .stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());

        return Order.builder()
                .items(orderItems)
                .recipient(getOrCreateRecipient(payload))
                .build();
    }

    private Recipient getOrCreateRecipient(CreateOrderPayload payload) {
        return recipientRepository
                .findByEmailIgnoreCase(payload.getRecipient().getEmail())
                .orElse(payload.getRecipient().toRecipient());
    }

    private OrderItem toOrderItem(CreateOrderItemPayload createOrderItemPayload) {
        int requestedQuantity = createOrderItemPayload.getQuantity();
        Book book = bookRepository.getOne(createOrderItemPayload.getBookId());
        if (book.getAvailable() >= requestedQuantity) {
            return new OrderItem(book, requestedQuantity);
        }
        throw new IllegalArgumentException("To many copies of book " + book.getId() +
                " requested: " + requestedQuantity + " of " + book.getAvailable() + " available ");
    }

    private Set<Book> reduceBooks(Order order) {
        return order.getItems()
                .stream()
                .map(item -> {
                    Book book = item.getBook();
                    book.setAvailable(book.getAvailable() - item.getQuantity());
                    return book;
                })
                .collect(Collectors.toSet());
    }

    private Set<Book> revokeBooks(Order order) {
        return order.getItems()
                .stream()
                .map(item -> {
                    Book book = item.getBook();
                    book.setAvailable(book.getAvailable() + item.getQuantity());
                    return book;
                })
                .collect(Collectors.toSet());
    }
}
