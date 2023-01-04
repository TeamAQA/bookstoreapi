package pl.akademiaqa.bos.autors.api.payload;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class UpdateAuthorPayload {

    @NotBlank
    String firstName;

    // TODO - BUG 1 - Można edytować autora z pustym polem lastName
    // @NotBlank
    String lastName;
}
