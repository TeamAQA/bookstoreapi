package pl.akademiaqa.bos.order.service.port;

import org.springframework.http.ResponseEntity;
import pl.akademiaqa.bos.order.api.payload.CreateOrderPayload;
import pl.akademiaqa.bos.order.api.payload.UpdateStatusPayload;
import pl.akademiaqa.bos.order.api.response.CreateOrderResponse;
import pl.akademiaqa.bos.order.domain.*;

import java.util.List;
import java.util.Optional;

public interface IOrderService {

    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);

    ResponseEntity deleteById(Long id);

    CreateOrderResponse createOrder(CreateOrderPayload payload);

    ResponseEntity<RichOrder> updateOrderStatus(Long id, UpdateStatusPayload payload);
}
