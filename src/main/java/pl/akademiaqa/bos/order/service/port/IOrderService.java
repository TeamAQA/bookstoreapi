package pl.akademiaqa.bookstore.order.service.port;

import org.springframework.http.ResponseEntity;
import pl.akademiaqa.bookstore.order.api.payload.CreateOrderPayload;
import pl.akademiaqa.bookstore.order.api.payload.UpdateStatusPayload;
import pl.akademiaqa.bookstore.order.api.response.CreateOrderResponse;
import pl.akademiaqa.bookstore.order.domain.*;

import java.util.List;
import java.util.Optional;

public interface IOrderService {

    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);

    ResponseEntity deleteById(Long id);

    CreateOrderResponse createOrder(CreateOrderPayload payload);

    ResponseEntity<RichOrder> updateOrderStatus(Long id, UpdateStatusPayload payload);
}
