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
public class UpdateBookPayload {
    @ValidTrim
    String title;
    @NotEmpty(message = "incorrect input data")
    Set<Long> authors;
    @NotNull(message = "incorrect input data")
    @YearMin(1900)
    Integer year;
    @NotNull(message = "incorrect input data")
    @DecimalMin(value = "1", message = "incorrect input data")
    // TODO - BUG 4 (PUT /books/:id) - Można edytować książkę z maksymalną ceną większą niż 1000, max cena to 100000
    @DecimalMax(value = "10000", message = "incorrect input data")
    @ValidPrice
    BigDecimal price;
    @NotNull(message = "incorrect input data")
    @Max(value = 10000, message = "incorrect input data")
    @Min(value = 1, message = "incorrect input data")
    Integer available;
}
