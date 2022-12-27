package pl.akademiaqa.bos.books.service.port;

import org.springframework.http.ResponseEntity;
import pl.akademiaqa.bos.books.api.payload.CreateUpdateBookPayload;
import pl.akademiaqa.bos.books.api.payload.UpdateBookCoverPayload;
import pl.akademiaqa.bos.books.api.response.CreateUpdateBookResponse;
import pl.akademiaqa.bos.books.api.response.PartialUpdateBookResponse;
import pl.akademiaqa.bos.books.domain.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IBookService {

    Optional<Book> findById(Long id);

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    Optional<Book> findOneByTitle(String title);

    List<Book> findAll();
    List<Book> admin_findAll();

    List<Book> findByTitleAndAuthor(String title, String author);

    CreateUpdateBookResponse createBook(CreateUpdateBookPayload payload);

    ResponseEntity removeById(Long id);

    CreateUpdateBookResponse updateBook(Long id, CreateUpdateBookPayload payload);

    CreateUpdateBookResponse updateBookCover(Long id, UpdateBookCoverPayload payload);

    PartialUpdateBookResponse partialUpdateBook(Long id, Map<Object, Object> fields);

    ResponseEntity removeBookCover(Long id);
}
