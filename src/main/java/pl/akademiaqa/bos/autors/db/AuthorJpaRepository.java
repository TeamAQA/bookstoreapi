package pl.akademiaqa.bos.autors.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akademiaqa.bos.autors.domain.Author;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {
}
