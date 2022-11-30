package pl.akademiaqa.bos.uploads.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.akademiaqa.bos.books.api.payload.UpdateBookCoverPayload;
import pl.akademiaqa.bos.uploads.db.UploadJpaRepository;
import pl.akademiaqa.bos.uploads.service.port.IUploadService;
import pl.akademiaqa.bos.uploads.domain.Upload;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UploadService implements IUploadService {

    private final UploadJpaRepository repository;

    @Override
    public Upload save(UpdateBookCoverPayload payload) {
        Upload upload = new Upload(
                payload.getFilename(),
                payload.getContentType(),
                payload.getFile()
        );
        repository.save(upload);
        return upload;
    }

    @Override
    public Optional<Upload> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}
