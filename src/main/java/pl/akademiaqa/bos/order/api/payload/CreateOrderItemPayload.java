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
    @Min(1)
    int quantity;
}
