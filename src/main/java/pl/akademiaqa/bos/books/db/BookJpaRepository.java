package pl.akademiaqa.bos.books.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.akademiaqa.bos.books.domain.Book;

import java.util.List;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

    @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.authors")
    List<Book> findAllEager();

    @Query(" SELECT b FROM Book b" +
            " where " +
            " lower(b.title) like lower(concat('%', :title, '%')) ")
    List<Book> findByTitle(@Param("title") String title);

    @Query(" SELECT b FROM Book b JOIN b.authors a " +
            " where " +
            " lower(a.fullName) like lower(concat('%', :author, '%')) ")
    List<Book> findByAuthor(@Param("author") String author);


    @Query(" SELECT b FROM Book b JOIN b.authors a " +
            " where " +
            " lower(a.fullName) like lower(concat('%', :author, '%')) " +
            " and " +
            " lower(b.title) like lower(concat('%', :title, '%')) ")
    List<Book> findByTitleAndAuthor(@Param("title") String title, @Param("author") String author);
}
