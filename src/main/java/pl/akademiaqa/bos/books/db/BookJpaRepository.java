package pl.akademiaqa.bookstore.books.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akademiaqa.bookstore.books.domain.Book;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
}
