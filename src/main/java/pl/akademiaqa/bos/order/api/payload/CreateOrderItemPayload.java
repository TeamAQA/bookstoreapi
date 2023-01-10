package pl.akademiaqa.bos.order.api.payload;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateOrderItemPayload {

    @NotNull
    Long bookId;

    @NotNull
    // TODO - BUG 6 - Można utworzyć zamówienie z ilością książek = 0
    @Min(0)
    int quantity;
}
