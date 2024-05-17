package pl.akademiaqa.bos.books.api.payload;

import lombok.Value;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Value
public class UpdateBookPayload {
    @NotBlank
    String title;
    @NotEmpty
    Set<Long> authors;
    @NotNull
    Integer year;
    @NotNull
    @DecimalMin(value = "1.00")
    // TODO - BUG 3 - Można edytować książkę z maksymalną ceną większą niż 1000.00, max cena to 100000.00
    @DecimalMax(value = "100000.00")
    BigDecimal price;
    @NotNull
    @PositiveOrZero
    Integer available;
}
