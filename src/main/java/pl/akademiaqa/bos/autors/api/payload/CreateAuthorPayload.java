package pl.akademiaqa.bos.autors.api.payload;

import lombok.Value;
import pl.akademiaqa.bos.validators.ValidName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class CreateAuthorPayload {

    @NotBlank(message = "can not be empty")
    @ValidName @Size(min = 3, max = 128)
    String firstName;

    @NotBlank(message = "can not be empty")
    @ValidName @Size(min = 3, max = 128)
    String lastName;
}
