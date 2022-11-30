package pl.akademiaqa.bookstore.books.service.port;

import org.springframework.http.ResponseEntity;
import pl.akademiaqa.bookstore.books.api.payload.CreateBookPayload;
import pl.akademiaqa.bookstore.books.api.payload.UpdateBookCoverPayload;
import pl.akademiaqa.bookstore.books.api.payload.UpdateBookPayload;
import pl.akademiaqa.bookstore.books.api.response.CreateBookResponse;
import pl.akademiaqa.bookstore.books.api.response.PartialUpdateBookResponse;
import pl.akademiaqa.bookstore.books.api.response.UpdateBookResponse;
import pl.akademiaqa.bookstore.books.domain.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IBookService {

    Optional<Book> findById(Long id);

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    Optional<Book> findOneByTitle(String title);

    Optional<Book> findOneByAuthor(String title);

    List<Book> findAll();

    Optional<Book> findOneByTitleAndAuthor(String title, String author);

    List<Book> findByTitleAndAuthor(String title, String author);

    CreateBookResponse createBook(CreateBookPayload payload);

    ResponseEntity removeById(Long id);

    UpdateBookResponse updateBook(Long id, UpdateBookPayload payload);

    UpdateBookResponse updateBookCover(Long id, UpdateBookCoverPayload payload);

    PartialUpdateBookResponse partialUpdateBook(Long id, Map<Object, Object> fields);

    ResponseEntity removeBookCover(Long id);
}
