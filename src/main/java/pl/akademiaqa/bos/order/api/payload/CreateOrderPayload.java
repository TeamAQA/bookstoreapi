package pl.akademiaqa.bos.order.api.payload;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class CreateOrderPayload {

    // TODO Usunąć walidację @NotEmpty, pozwoli na utworzenie order z pustą listą.
    @Singular
    @Valid @NotNull @NotEmpty
    List<CreateOrderItemPayload> items;
    @Valid @NotNull
    CreateRecipientPayload recipient;
}
