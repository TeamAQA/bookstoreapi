package pl.akademiaqa.bookstore.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import pl.akademiaqa.bookstore.books.api.payload.CreateBookPayload;
import pl.akademiaqa.bookstore.books.api.payload.UpdateBookCoverPayload;
import pl.akademiaqa.bookstore.books.api.payload.UpdateBookPayload;
import pl.akademiaqa.bookstore.books.api.response.CreateBookResponse;
import pl.akademiaqa.bookstore.books.api.response.PartialUpdateBookResponse;
import pl.akademiaqa.bookstore.books.api.response.UpdateBookResponse;
import pl.akademiaqa.bookstore.books.service.port.IBookService;
import pl.akademiaqa.bookstore.books.db.BookJpaRepository;
import pl.akademiaqa.bookstore.books.domain.Book;
import pl.akademiaqa.bookstore.commons.StringBuilderPlus;
import pl.akademiaqa.bookstore.uploads.service.port.IUploadService;
import pl.akademiaqa.bookstore.uploads.domain.Upload;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService {
    private final BookJpaRepository repository;
    private final IUploadService upload;

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().contains(title))
                .findFirst();
    }

    @Override
    public Optional<Book> findOneByAuthor(String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().contains(author))
                .findFirst();
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .findFirst();
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public CreateBookResponse createBook(CreateBookPayload payload) {
        Book savedBook = repository.save(payload.toBook());
        if (savedBook == null) {
            return CreateBookResponse.failure("Can not create a book");
        }
        return CreateBookResponse.success(savedBook.getId());
    }

    @Override
    public ResponseEntity removeById(Long id) {
        Optional<Book> book = repository.findById(id);
        if (book.isPresent()) {
            repository.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public UpdateBookResponse updateBook(Long id, UpdateBookPayload payload) {
        return repository.findById(id)
                .map(book -> {
                    Book updatedBook = payload.toUpdatedBook(book);
                    repository.save(updatedBook);
                    return UpdateBookResponse.success(updatedBook.getId());
                })
                .orElseGet(() -> UpdateBookResponse.failure("Can not find a book with id: " + id));
    }

    @Override
    public UpdateBookResponse updateBookCover(Long id, UpdateBookCoverPayload payload) {
        return repository.findById(id)
                .map(book -> {
                    if (payload.getFilename().isBlank()) {
                        throw new IllegalArgumentException("Invalid file");
                    }
                    Upload savedUpload = upload.save(new UpdateBookCoverPayload(payload.getFile(), payload.getContentType(), payload.getFilename()));
                    book.setCoverId(savedUpload.getId());
                    repository.save(book);
                    return UpdateBookResponse.success(book.getId());
                })
                .orElseGet(() -> UpdateBookResponse.failure("Can not find a book with id: " + id));
    }

    @Override
    public PartialUpdateBookResponse partialUpdateBook(Long id, Map<Object, Object> fields) {

        Optional<Book> existingBook = repository.findById(id);
        if (existingBook.isEmpty()) {
            return PartialUpdateBookResponse.failure("Can not find a book with id: " + id);
        }

        AtomicBoolean isError = new AtomicBoolean(false);
        StringBuilderPlus errors = new StringBuilderPlus();

        return existingBook
                .map(book -> {
                    fields.forEach((key, value) -> {
                        Field field = ReflectionUtils.findField(Book.class, (String) key);
                        field.setAccessible(true);
                        if (field.getName().equals("id")) {
                        } else if (field.getName().equals("price")) {
                            if (isNullOrEmpty(value)) {
                                isError.set(true);
                                errors.appendLine("price must not be null");
                                return;
                            }
                            if (Double.valueOf(String.valueOf(value)) < 1) {
                                isError.set(true);
                                errors.appendLine("price must be greater than or equal to 1.00");
                                return;
                            }
                            if (Double.valueOf(String.valueOf(value)) > 1000) {
                                isError.set(true);
                                errors.appendLine("price must be less than or equal to 1000.00");
                                return;
                            }
                            ReflectionUtils.setField(field, book, new BigDecimal(String.valueOf(value)));
                        } else if (field.getName().equals("year")) {
                            if (isNullOrEmpty(value)) {
                                isError.set(true);
                                errors.appendLine("year must not be null");
                                return;
                            }
                            if (value.getClass() != Integer.class) {
                                isError.set(true);
                                errors.appendLine("year must be a number");
                                return;
                            }
                            ReflectionUtils.setField(field, book, value);
                        } else if (field.getName().equals("title")) {
                            if (isNullOrEmpty(value)) {
                                isError.set(true);
                                errors.appendLine("title must not be blank");
                                return;
                            }
                            if (value.getClass() != String.class) {
                                isError.set(true);
                                errors.appendLine("title must be a string");
                                return;
                            }
                            ReflectionUtils.setField(field, book, value);
                        } else if (field.getName().equals("author")) {
                            if (isNullOrEmpty(value)) {
                                isError.set(true);
                                errors.appendLine("author must not be blank");
                                return;
                            }
                            if (value.getClass() != String.class) {
                                isError.set(true);
                                errors.appendLine("author must be a string");
                                return;
                            }
                            ReflectionUtils.setField(field, book, value);
                        } else {
                            ReflectionUtils.setField(field, book, value);
                        }
                    });

                    if (isError.get()) {
                        throw new IllegalArgumentException(errors.toString());
                    } else {
                        repository.save(book);
                    }

                    return PartialUpdateBookResponse.success(book.getId());

                })
                .orElseGet(() -> PartialUpdateBookResponse.failure("Can not find a book with id: " + id));
    }

    private boolean isNullOrEmpty(Object value) {
        return String.valueOf(value).isBlank() || String.valueOf(value).equals("null") || String.valueOf(value) == null;
    }

    @Override
    public ResponseEntity removeBookCover(Long id) {
        repository.findById(id)
                .ifPresent(book -> {
                    if (book.getCoverId() != null) {
                        upload.removeById(book.getCoverId());
                        book.setCoverId(null);
                        repository.save(book);
                    }
                });

        return ResponseEntity.noContent().build();
    }
}
