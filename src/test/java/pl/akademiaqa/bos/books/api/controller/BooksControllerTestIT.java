package pl.akademiaqa.bos.books.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import pl.akademiaqa.bos.autors.db.AuthorJpaRepository;
import pl.akademiaqa.bos.autors.domain.Author;
import pl.akademiaqa.bos.books.api.payload.CreateBookPayload;
import pl.akademiaqa.bos.books.domain.RestBook;
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

        CreateBookPayload effectiveJava = new CreateBookPayload(
                "Effective Java",
                Set.of(joshua.getId()),
                2005,
                new BigDecimal("79.00"),
                50L);

        CreateBookPayload javaPuzzlers = new CreateBookPayload(
                "Java Puzzlers",
                Set.of(joshua.getId(), neal.getId()),
                2018,
                new BigDecimal("99.00"),
                50L);

        bookService.createBook(effectiveJava);
        bookService.createBook(javaPuzzlers);

        List<RestBook> allBooks = controller.getAll(mockRequest(), Optional.empty(), Optional.empty());

        assertThat(allBooks).isNotEmpty();
        assertThat(allBooks.size()).isEqualTo(2);
    }

    private MockHttpServletRequest mockRequest() {
        return new MockHttpServletRequest();
    }
}
