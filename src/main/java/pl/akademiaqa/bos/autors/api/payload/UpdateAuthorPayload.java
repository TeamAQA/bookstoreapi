package pl.akademiaqa.bos.autors.api.payload;

import lombok.Value;
import pl.akademiaqa.bos.validators.ValidName;
import pl.akademiaqa.bos.validators.ValidText;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class UpdateAuthorPayload {

    @ValidName
    String firstName;

    // TODO - BUG 1 (PUT /authors) - Nie można edytować nazwiska autora, które zawiera polskie znaki.
    //  Przykład: Lisowski - ok, Lisówkie - nie ok.
    // Tu powinna być ustawiona validacja @ValidName a nie @ValidText
    @ValidText
    String lastName;
}
