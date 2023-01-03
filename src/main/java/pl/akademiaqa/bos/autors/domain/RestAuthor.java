package pl.akademiaqa.bos.autors.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;

@Value
public class RestAuthor {
    String firstName;
    String lastName;

    @JsonIgnore
    String fullName;
}
