package pl.akademiaqa.bookstore.uploads.service;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import pl.akademiaqa.bookstore.books.api.payload.UpdateBookCoverPayload;
import pl.akademiaqa.bookstore.uploads.service.port.IUploadService;
import pl.akademiaqa.bookstore.uploads.domain.Upload;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UploadService implements IUploadService {
    private final Map<String, Upload> storage = new ConcurrentHashMap<>();

    @Override
    public Upload save(UpdateBookCoverPayload payload) {
        String newId = RandomStringUtils.randomAlphanumeric(10).toLowerCase();
        Upload upload = new Upload(
                newId,
                payload.getFile(),
                payload.getContentType(),
                payload.getFilename(),
                LocalDateTime.now()
        );
        storage.put(upload.getId(), upload);
        return upload;
    }

    @Override
    public Optional<Upload> getById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void removeById(String id) {
        storage.remove(id);
    }
}
