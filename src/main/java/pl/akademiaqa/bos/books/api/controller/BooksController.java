package pl.akademiaqa.bos.books.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiaqa.bos.books.api.payload.CreateUpdateBookPayload;
import pl.akademiaqa.bos.books.api.payload.UpdateBookCoverPayload;
import pl.akademiaqa.bos.books.service.port.IBookService;
import pl.akademiaqa.bos.books.domain.Book;
import pl.akademiaqa.bos.web.CreatedURI;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/books")
@RestController
@RequiredArgsConstructor
public class BooksController {
    private final IBookService books;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getAll(@RequestParam Optional<String> title, @RequestParam Optional<String> author) {
        if (title.isPresent() && author.isPresent()) {
            return books.findByTitleAndAuthor(title.get(), author.get());
        } else if (title.isPresent()) {
            return books.findByTitle(title.get());
        } else if (author.isPresent()) {
            return books.findByAuthor(author.get());
        }
        return books.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return books.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createBook(@Valid @RequestBody CreateUpdateBookPayload payload) {
        return books.createBook(payload)
                .handle(
                        bookId -> ResponseEntity.created(bookUri(bookId)).body(books.findById(bookId)),
                        error -> ResponseEntity.badRequest().body(error)
                );
    }

    private URI bookUri(Long bookId) {
        return new CreatedURI("/" + bookId).uri();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateBook(@PathVariable Long id, @Valid @RequestBody CreateUpdateBookPayload payload) {
        return books.updateBook(id, payload)
                .handle(
                        bookId -> {
                            Optional<Book> book = books.findById(bookId);
                            if (book.get().isPut()) {
                                return ResponseEntity.ok(book);
                            } else {
                                return ResponseEntity.created(putBookUri()).body(book);
                            }
                        },
                        error -> ResponseEntity.notFound().build()
                );
    }

    private URI putBookUri() {
        return new CreatedURI("").uri();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> partialUpdateBook(@PathVariable Long id, @RequestBody Map<Object, Object> fields) {
        return books.partialUpdateBook(id, fields)
                .handle(
                        bookId -> ResponseEntity.ok(books.findById(bookId)),
                        error -> ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        books.removeById(id);
    }

    @PatchMapping(value = "/{id}/cover", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateBookCover(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return books.updateBookCover(id, new UpdateBookCoverPayload(file.getBytes(), file.getContentType(), file.getOriginalFilename()))
                .handle(
                        bookId -> ResponseEntity.ok(books.findById(bookId)),
                        error -> ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping(value = "/{id}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookCover(@PathVariable Long id) {
        books.removeBookCover(id);
    }
}
