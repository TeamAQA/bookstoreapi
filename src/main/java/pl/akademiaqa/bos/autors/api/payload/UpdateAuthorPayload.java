package pl.akademiaqa.bos.autors.api.payload;

import lombok.Value;
import pl.akademiaqa.bos.validators.ValidName;
import pl.akademiaqa.bos.validators.ValidText;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class UpdateAuthorPayload {

    @NotBlank @ValidName @Size(min = 3, max = 128)
    String firstName;

    // TODO - BUG 1 - Nie można edytować nazwiska autora, które zawiera polskie znaki.
    // Tu powinna być ustawiona validacja @ValidName a nie @ValidText
    @NotBlank @ValidText @Size(min = 3, max = 128)
    String lastName;
}
