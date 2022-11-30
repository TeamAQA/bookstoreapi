package pl.akademiaqa.bookstore.uploads.service.port;

import pl.akademiaqa.bookstore.books.api.payload.UpdateBookCoverPayload;
import pl.akademiaqa.bookstore.uploads.domain.Upload;

import java.util.Optional;

public interface IUploadService {

    Upload save(UpdateBookCoverPayload payload);

    Optional<Upload> getById(String id);

    void removeById(String id);
}
