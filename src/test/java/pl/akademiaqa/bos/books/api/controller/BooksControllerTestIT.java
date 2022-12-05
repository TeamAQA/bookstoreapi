package pl.akademiaqa.bos.books.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import pl.akademiaqa.bos.autors.db.AuthorJpaRepository;
import pl.akademiaqa.bos.autors.domain.Author;
import pl.akademiaqa.bos.books.api.payload.CreateUpdateBookPayload;
import pl.akademiaqa.bos.books.domain.Book;
import pl.akademiaqa.bos.books.service.port.IBookService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
class BooksControllerTestIT {

    @Autowired
    BooksController controller;
    @Autowired
    IBookService bookService;
    @Autowired
    AuthorJpaRepository authorRepository;

    @Test
    public void getAllBooks() {

        Author joshua = new Author("Joshua", "Bloch");
        Author neal = new Author("Neal", "Gafter");
        Author jamesClear = new Author("James", "Clear");
        authorRepository.save(joshua);
        authorRepository.save(neal);
        authorRepository.save(jamesClear);

        CreateUpdateBookPayload effectiveJava = new CreateUpdateBookPayload(
                "Effective Java",
                Set.of(joshua.getId()),
                2005,
                new BigDecimal("79.00"),
                50L);

        CreateUpdateBookPayload javaPuzzlers = new CreateUpdateBookPayload(
                "Java Puzzlers",
                Set.of(joshua.getId(), neal.getId()),
                2018,
                new BigDecimal("99.00"),
                50L);

        bookService.createBook(effectiveJava);
        bookService.createBook(javaPuzzlers);

        List<Book> allBooks = controller.getAll(Optional.empty(), Optional.empty());

        assertThat(allBooks).isNotEmpty();
        assertThat(allBooks.size()).isEqualTo(2);
    }
}