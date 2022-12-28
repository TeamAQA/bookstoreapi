package pl.akademiaqa.bos.books.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import pl.akademiaqa.bos.autors.db.AuthorJpaRepository;
import pl.akademiaqa.bos.autors.domain.Author;
import pl.akademiaqa.bos.books.api.payload.CreateUpdateBookPayload;
import pl.akademiaqa.bos.books.api.payload.UpdateBookCoverPayload;
import pl.akademiaqa.bos.books.api.response.CreateUpdateBookResponse;
import pl.akademiaqa.bos.books.api.response.PartialUpdateBookResponse;
import pl.akademiaqa.bos.books.service.port.IBookService;
import pl.akademiaqa.bos.books.db.BookJpaRepository;
import pl.akademiaqa.bos.books.domain.Book;
import pl.akademiaqa.bos.commons.StringBuilderPlus;
import pl.akademiaqa.bos.uploads.service.port.IUploadService;
import pl.akademiaqa.bos.uploads.domain.Upload;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static pl.akademiaqa.bos.commons.IsNullOrEmpty.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService implements IBookService {
    private final BookJpaRepository repository;
    private final AuthorJpaRepository authorRepository;
    private final IUploadService upload;

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return repository.findByTitle(title);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return repository.findByAuthor(author);
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return repository.findByTitle(title)
                .stream()
                .findFirst();
    }

    @Override
    public List<Book> findAll() {
        return repository.findAllEager().stream().sorted().collect(Collectors.toList());
    }

    @Override
    public List<Book> admin_findAll() {
        return repository.admin_findAllBooks().stream().sorted().collect(Collectors.toList());
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return repository.findByTitleAndAuthor(title, author);
    }

    @Override
    @Transactional
    public CreateUpdateBookResponse createBook(CreateUpdateBookPayload payload) {
        Book savedBook = repository.save(toBook(payload));
        if (savedBook == null) {
            return CreateUpdateBookResponse.failure("Can not create a book");
        }
        return CreateUpdateBookResponse.success(savedBook.getId());
    }

    public CreateUpdateBookResponse createBook(Long id, CreateUpdateBookPayload payload) {
        Book savedBook = repository.save(toBook(id, payload));
        if (savedBook == null) {
            return CreateUpdateBookResponse.failure("Can not create a book");
        }
        return CreateUpdateBookResponse.success(savedBook.getId());
    }

    private Book toBook(CreateUpdateBookPayload payload) {
        Book book = new Book(payload.getTitle(), payload.getYear(), payload.getPrice(), payload.getAvailable());
        Set<Author> authors = payload.getAuthors()
                .stream()
                .map(authorId ->
                        authorRepository.findById(authorId)
                                .orElseThrow(() -> new IllegalStateException("Can not find author with given id: " + authorId))
                ).collect(Collectors.toSet());
        updateBookAuthors(book, authors);

        return book;
    }

    private Book toBook(Long id, CreateUpdateBookPayload payload) {
        Book book = new Book(id, payload.getTitle(), payload.getYear(), payload.getPrice(), payload.getAvailable());
        Set<Author> authors = payload.getAuthors()
                .stream()
                .map(authorId ->
                        authorRepository.findById(authorId)
                                .orElseThrow(() -> new IllegalStateException("Can not find author with given id: " + authorId))
                ).collect(Collectors.toSet());
        updateBookAuthors(book, authors);

        return book;
    }

    private void updateBookAuthors(Book book, Set<Author> authors) {
        book.removeAuthors();
        authors.forEach(book::addAuthor);
        log.info(book.toString());
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
    @Transactional
    public CreateUpdateBookResponse updateBook(Long id, CreateUpdateBookPayload payload) {
        return repository.findById(id)
                .map(book -> {
                    Book updatedBook = toUpdatedBook(payload, book);
                    updatedBook.setPut(true);
                    repository.save(updatedBook);
                    return CreateUpdateBookResponse.success(updatedBook.getId());
                })
                .orElseGet(() -> createBook(id, payload));
    }

    private Book toUpdatedBook(CreateUpdateBookPayload payload, Book book) {
        Set<Author> authors = payload.getAuthors()
                .stream()
                .map(authorId -> authorRepository.findById(authorId)
                        .orElseThrow(() -> new IllegalStateException("Can not find author with given id: " + authorId))
                ).collect(Collectors.toSet());


        book.setTitle(payload.getTitle());
        book.setYear(payload.getYear());
        book.setPrice(payload.getPrice());
        book.setAvailable(payload.getAvailable());
        updateBookAuthors(book, authors);

        return book;
    }

    @Override
    public CreateUpdateBookResponse updateBookCover(Long id, UpdateBookCoverPayload payload) {
        return repository.findById(id)
                .map(book -> {
                    if (payload.getFilename().isBlank()) {
                        throw new IllegalArgumentException("Invalid file");
                    }
                    Upload savedUpload = upload.save(new UpdateBookCoverPayload(payload.getFile(), payload.getContentType(), payload.getFilename()));
                    book.setCoverId(savedUpload.getId());
                    repository.save(book);
                    return CreateUpdateBookResponse.success(book.getId());
                })
                .orElseGet(() -> CreateUpdateBookResponse.failure("Can not find a book with id: " + id));
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
                            // ignore id
                        } else if (field.getName().equals("price")) {
                            if (value == null) {
                                isError.set(true);
                                errors.appendLine("price must not be null");
                                return;
                            }

                            if (value.getClass() == LinkedHashMap.class) {
                                BigDecimal price = book.getPrice();
                                BigDecimal newPrice = price.add(new BigDecimal(1));
                                ReflectionUtils.setField(field, book, newPrice);
                                return;
                            }

                            if (String.valueOf(value).equals("null")) {
                                isError.set(true);
                                errors.appendLine("price must not be null");
                                return;
                            }
                            if (String.valueOf(value).isBlank()) {
                                isError.set(true);
                                errors.appendLine("price must not be empty");
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

                        } else if (field.getName().equals("authors")) {
                            if (value == null) {
                                isError.set(true);
                                errors.appendLine("authors must not be blank");
                                return;
                            }
                            if (value.getClass() != ArrayList.class) {
                                isError.set(true);
                                errors.appendLine("authors must be an array");
                                return;
                            }

                            Set<Author> authors = ((ArrayList<Integer>) value).stream()
                                    .map(authorId -> authorRepository.findById(authorId.longValue())
                                            .orElseThrow(() -> new IllegalStateException("Can not find author with given id: " + authorId))
                                    ).collect(Collectors.toSet());

                            ReflectionUtils.setField(field, book, authors);
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
