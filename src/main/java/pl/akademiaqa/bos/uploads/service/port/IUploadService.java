package pl.akademiaqa.bos.uploads.service.port;

import pl.akademiaqa.bos.books.api.payload.UpdateBookCoverPayload;
import pl.akademiaqa.bos.uploads.domain.Upload;

import java.util.Optional;

public interface IUploadService {

    Upload save(UpdateBookCoverPayload payload);

    Optional<Upload> getById(Long id);

    void removeById(Long id);
}
