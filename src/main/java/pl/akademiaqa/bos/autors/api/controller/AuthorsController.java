package pl.akademiaqa.bos.autors.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.akademiaqa.bos.autors.api.payload.CreateAuthorPayload;
import pl.akademiaqa.bos.autors.api.payload.UpdateAuthorPayload;
import pl.akademiaqa.bos.autors.domain.Author;
import pl.akademiaqa.bos.autors.service.port.IAuthorService;
import pl.akademiaqa.bos.web.CreatedURI;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/authors")
@RestController
@RequiredArgsConstructor
public class AuthorsController {

    private final IAuthorService authors;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Author> getAll(@RequestParam Optional<String> firstName, @RequestParam Optional<String> lastName) {
        if (firstName.isPresent() && lastName.isPresent()) {
            return authors.findByFirstNameAndLastName(firstName.get(), lastName.get());
        } else if (firstName.isPresent()) {
            return authors.findByFirstName(firstName.get());
        } else if (lastName.isPresent()) {
            return authors.findByLastName(lastName.get());
        }
        return authors.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable Long id) {
        return authors.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createAuthor(@Valid @RequestBody CreateAuthorPayload payload) {
        return authors.createAuthor(payload)
                .handle(
                        authorId -> ResponseEntity.created(authorUri(authorId)).body(authors.findById(authorId)),
                        error -> ResponseEntity.badRequest().body(error)
                );
    }

    private URI authorUri(Long authorId) {
        return new CreatedURI("/" + authorId).uri();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateAuthor(@PathVariable Long id, @Valid @RequestBody UpdateAuthorPayload payload) {
        return authors.updateAuthor(id, payload)
                .handle(
                        authorId -> ResponseEntity.ok(authors.findById(authorId)),
                        error -> ResponseEntity.notFound().build()
                );
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> partialUpdateAuthor(@PathVariable Long id, @RequestBody Map<Object, Object> fields) {
        return authors.partialUpdateAuthor(id, fields)
                .handle(
                        authorId -> ResponseEntity.ok(authors.findById(authorId)),
                        error -> ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        authors.removeById(id);
    }
}
