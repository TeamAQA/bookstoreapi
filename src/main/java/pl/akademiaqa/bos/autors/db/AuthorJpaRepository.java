package pl.akademiaqa.bos.autors.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akademiaqa.bos.autors.domain.Author;

import java.util.Optional;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFullNameIgnoreCase(String fullName);
}
