package pl.akademiaqa.bookstore.order.api.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateStatusPayload {

    @NotBlank
    String status;
}
