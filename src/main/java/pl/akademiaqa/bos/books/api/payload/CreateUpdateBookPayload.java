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
    @DecimalMin(value = "1.00")
    @DecimalMax(value = "1000.00")
    BigDecimal price;
}
