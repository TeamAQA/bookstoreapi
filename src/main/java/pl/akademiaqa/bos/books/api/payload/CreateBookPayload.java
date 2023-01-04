package pl.akademiaqa.bos.books.api.payload;

import lombok.Value;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Value
public class CreateBookPayload {
    @NotBlank
    String title;
    @NotEmpty
    Set<Long> authors;
    @NotNull
    Integer year;
    @NotNull
    // TODO - BUG 2 - Można utworzyć książkę z minimalną ceną mniejszą niż 1.00. Min cena to 0.01
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "1000.00")
    BigDecimal price;
    @NotNull
    @PositiveOrZero
    Long available;
}
