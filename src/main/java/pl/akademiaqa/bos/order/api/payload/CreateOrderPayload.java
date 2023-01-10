package pl.akademiaqa.bos.order.api.payload;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class CreateOrderPayload {

    // TODO - BUG 4 - Można utworzyć zamówienie z pustą listą items.
    @Singular
    @Valid @NotNull
    List<CreateOrderItemPayload> items;
    @Valid @NotNull
    CreateRecipientPayload recipient;
}
