package pl.akademiaqa.bos.books.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.akademiaqa.bos.autors.domain.Author;
import pl.akademiaqa.bos.autors.domain.RestAuthor;
import pl.akademiaqa.bos.books.api.payload.CreateUpdateBookPayload;
import pl.akademiaqa.bos.books.api.payload.UpdateBookCoverPayload;
import pl.akademiaqa.bos.books.domain.RestBook;
import pl.akademiaqa.bos.books.service.port.IBookService;
import pl.akademiaqa.bos.books.domain.Book;
import pl.akademiaqa.bos.web.CreatedURI;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/books")
@RestController
@RequiredArgsConstructor
public class BooksController {
    private final IBookService bookService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RestBook> getAll(
            HttpServletRequest request,
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> author) {
        List<Book> books;
        if (title.isPresent() && author.isPresent()) {
            books = bookService.findByTitleAndAuthor(title.get(), author.get());
        } else if (title.isPresent()) {
            books = bookService.findByTitle(title.get());
        } else if (author.isPresent()) {
            books = bookService.findByAuthor(author.get());
        } else {
            books = bookService.findAll();
        }
        return books.stream()
                .map(book -> toRestBook(book, request))
                .collect(Collectors.toList());
    }

    @GetMapping("/admin_books")
    @ResponseStatus(HttpStatus.OK)
    public List<RestBook> admin_getAll(HttpServletRequest request) {
        return bookService.admin_findAll().stream()
                .map(book -> toRestBook(book, request))
                .collect(Collectors.toList());
    }

    private RestBook toRestBook(Book book, HttpServletRequest request) {
        String coverUrl = Optional.ofNullable(book.getCoverId()).map(
                        coverId -> ServletUriComponentsBuilder
                                .fromContextPath(request)
                                .path("/uploads/{id}/file")
                                .build(coverId)
                                .toASCIIString())
                .orElse(null);

        return new RestBook(
                book.getId(),
                book.getTitle(),
                book.getYear(),
                book.getPrice(),
                coverUrl,
                book.getAvailable(),
                toRestAuthors(book.getAuthors())
        );
    }

    private Set<RestAuthor> toRestAuthors(Set<Author> authors) {
        return authors.stream()
                .map(author -> new RestAuthor(
                        author.getFirstName(),
                        author.getLastName(),
                        author.getFullName()
                ))
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createBook(@Valid @RequestBody CreateUpdateBookPayload payload) {
        return bookService.createBook(payload)
                .handle(
                        bookId -> ResponseEntity.created(bookUri(bookId)).body(bookService.findById(bookId)),
                        error -> ResponseEntity.badRequest().body(error)
                );
    }

    private URI bookUri(Long bookId) {
        return new CreatedURI("/" + bookId).uri();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateBook(@PathVariable Long id, @Valid @RequestBody CreateUpdateBookPayload payload) {
        return bookService.updateBook(id, payload)
                .handle(
                        bookId -> ResponseEntity.ok(bookService.findById(bookId)),
                        error -> ResponseEntity.notFound().build()
                );
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> partialUpdateBook(@PathVariable Long id, @RequestBody Map<Object, Object> fields) {
        return bookService.partialUpdateBook(id, fields)
                .handle(
                        bookId -> ResponseEntity.ok(bookService.findById(bookId)),
                        error -> ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        bookService.removeById(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping(value = "/{id}/cover", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateBookCover(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return bookService.updateBookCover(id, new UpdateBookCoverPayload(file.getBytes(), file.getContentType(), file.getOriginalFilename()))
                .handle(
                        bookId -> ResponseEntity.ok(bookService.findById(bookId)),
                        error -> ResponseEntity.notFound().build()
                );
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/{id}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookCover(@PathVariable Long id) {
        bookService.removeBookCover(id);
    }
}
