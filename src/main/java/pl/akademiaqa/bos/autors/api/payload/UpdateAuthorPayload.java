package pl.akademiaqa.bos.autors.api.payload;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class UpdateAuthorPayload {

    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
}
