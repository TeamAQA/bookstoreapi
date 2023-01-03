package pl.akademiaqa.bos.autors.api.payload;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class UpdateAuthorPayload {

    @NotBlank
    String firstName;

    // TODO - BUG 1 - EDYCJA AUTORA Z PUSTYM POLEM lastName
//    @NotBlank
    String lastName;
}
