package pl.akademiaqa.bos.autors.api.payload;

import lombok.Value;
import pl.akademiaqa.bos.validators.ValidName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class CreateAuthorPayload {

    @ValidName
    String firstName;

    @ValidName
    String lastName;
}
