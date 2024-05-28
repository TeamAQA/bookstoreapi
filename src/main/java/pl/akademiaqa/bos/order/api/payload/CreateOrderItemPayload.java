package pl.akademiaqa.bos.order.api.payload;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateOrderItemPayload {

    @NotNull(message = "incorrect input data")
    Long bookId;

    @NotNull(message = "incorrect input data")
    @Min(value = 1, message = "incorrect input data")
    int quantity;
}
