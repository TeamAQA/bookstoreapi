package pl.akademiaqa.bos.order.api.payload;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateOrderItemPayload {

    @NotNull(message = "can not be empty")
    Long bookId;

    @NotNull(message = "can not be empty")
    @Min(value = 1, message = "must be greater than or equal to 1")
    int quantity;
}
