package pl.akademiaqa.bos.order.api.payload;

import lombok.Builder;
import lombok.Data;
import pl.akademiaqa.bos.order.domain.Recipient;
import pl.akademiaqa.bos.validators.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CreateRecipientPayload {

    @ValidName
    String name;
    @ValidPhoneNumber
    String phone;
    @ValidStreet
    String street;
    @ValidName
    String city;
    @ValidZipCode
    String zipCode;
    @ValidEmail
    String email;

    public Recipient toRecipient() {
        return new Recipient(name, phone, street, city, zipCode, email);
    }
}
