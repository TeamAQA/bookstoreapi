package pl.akademiaqa.bos.uploads.api.response;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class UploadResponse {
    Long id;
    String contentType;
    String filename;
    LocalDateTime createdAt;
}