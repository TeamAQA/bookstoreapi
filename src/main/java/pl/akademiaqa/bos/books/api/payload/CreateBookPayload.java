package pl.akademiaqa.bos.books.api.payload;

import lombok.Value;
import pl.akademiaqa.bos.validators.ValidName;
import pl.akademiaqa.bos.validators.ValidPrice;
import pl.akademiaqa.bos.validators.ValidTrim;
import pl.akademiaqa.bos.validators.YearMin;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Value
public class CreateBookPayload {
    @ValidTrim
    String title;
    @NotEmpty(message = "incorrect input data")
    Set<Long> authors;
    @NotNull(message = "incorrect input data")
    @YearMin(1900)
    Integer year;
    @NotNull(message = "incorrect input data")
    // TODO - BUG 3 (POST /books) - Można utworzyć książkę z minimalną ceną mniejszą niż 1.00.
    //  Min cena to 0.01
    @DecimalMin(value = "0.01", message = "incorrect input data")
    @DecimalMax(value = "1000", message = "incorrect input data")
    @ValidPrice
    BigDecimal price;
    @NotNull(message = "incorrect input data")
    @Max(10000)
    @Min(1)
    Integer available;
}
