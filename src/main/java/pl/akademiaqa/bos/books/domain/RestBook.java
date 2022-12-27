package pl.akademiaqa.bos.books.domain;


import lombok.Value;
import pl.akademiaqa.bos.autors.domain.RestAuthor;

import java.math.BigDecimal;
import java.util.Set;

@Value
public class RestBook {
    Long id;
    String title;
    Integer year;
    BigDecimal price;
    String coverUrl;
    Long available;
    Set<RestAuthor> authors;
}
