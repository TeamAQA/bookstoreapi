package pl.akademiaqa.bos.order.api.payload;

import lombok.Builder;
import lombok.Data;
import pl.akademiaqa.bos.order.domain.Recipient;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CreateRecipientPayload {

    @NotBlank
    String name;
    @NotBlank
    String phone;
    @NotBlank
    String street;
    @NotBlank
    String city;
    @NotBlank
    String zipCode;
    @Email
    String email;

    public Recipient toRecipient() {
        return new Recipient(name, phone, street, city, zipCode, email);
    }
}
