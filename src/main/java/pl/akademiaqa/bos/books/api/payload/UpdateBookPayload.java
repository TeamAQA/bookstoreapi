package pl.akademiaqa.bos.books.api.payload;

import lombok.Value;
import pl.akademiaqa.bos.validators.ValidPrice;
import pl.akademiaqa.bos.validators.ValidTrim;
import pl.akademiaqa.bos.validators.YearMin;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Value
public class UpdateBookPayload {
    @NotBlank(message = "can not be empty")
    @ValidTrim
    String title;
    @NotEmpty(message = "can not be empty")
    Set<Long> authors;
    @NotNull(message = "can not be empty")
    @YearMin(1900)
    Integer year;
    @NotNull(message = "can not be empty")
    @DecimalMin(value = "1")
    // TODO - BUG 4 (PUT /books/:id) - Można edytować książkę z maksymalną ceną większą niż 1000, max cena to 100000
    @DecimalMax(value = "10000")
    @ValidPrice
    BigDecimal price;
    @NotNull(message = "can not be empty")
    @Max(10000)
    @Min(1)
    Integer available;
}
