package pl.akademiaqa.bos.books.api.payload;

import lombok.Value;
import pl.akademiaqa.bos.validators.ValidPrice;
import pl.akademiaqa.bos.validators.ValidTrim;
import pl.akademiaqa.bos.validators.YearMin;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Value
public class CreateBookPayload {
    @NotBlank(message = "can not be empty")
    @ValidTrim
    String title;
    @NotEmpty(message = "can not be empty")
    Set<Long> authors;
    @NotNull(message = "can not be empty")
    @YearMin(1900)
    Integer year;
    @NotNull(message = "can not be empty")
    // TODO - BUG 3 (POST /books) - Można utworzyć książkę z minimalną ceną mniejszą niż 1.00.
    //  Min cena to 0.01
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "1000")
    @ValidPrice
    BigDecimal price;
    @NotNull(message = "can not be empty")
    @Max(10000)
    @Min(1)
    Integer available;
}
