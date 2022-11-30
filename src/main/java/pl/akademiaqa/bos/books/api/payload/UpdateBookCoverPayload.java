package pl.akademiaqa.bos.books.api.payload;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UpdateBookCoverPayload {
    byte[] file;
    String contentType;
    String filename;
}
