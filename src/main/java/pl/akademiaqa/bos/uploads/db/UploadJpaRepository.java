package pl.akademiaqa.bos.uploads.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akademiaqa.bos.uploads.domain.Upload;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {
}
