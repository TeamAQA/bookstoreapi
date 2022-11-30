package pl.akademiaqa.bookstore.order.api.payload;

import lombok.*;
import pl.akademiaqa.bookstore.order.domain.Order;
import pl.akademiaqa.bookstore.order.domain.OrderItem;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CreateOrderPayload {

    // TODO Usunąć walidację @NotEmpty, pozwoli na utworzenie order z pustą listą.
    @Singular
    @Valid @NotNull @NotEmpty
    List<CreateOrderItemPayload> items;
    @Valid @NotNull
    CreateRecipientPayload recipient;

    public Order toOrder() {
        List<OrderItem> orderItems = items
                .stream()
                .map(item -> new OrderItem(item.bookId, item.quantity))
                .collect(Collectors.toList());

        return Order.builder()
                .items(orderItems)
                .recipient(recipient.toRecipient())
                .build();
    }
}
