package pl.akademiaqa.bookstore.uploads.api.response;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class UploadResponse {
    String id;
    String contentType;
    String filename;
    LocalDateTime createdAt;
}