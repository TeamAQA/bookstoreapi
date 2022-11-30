package pl.akademiaqa.bos.autors.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import pl.akademiaqa.bos.autors.api.payload.CreateAuthorPayload;
import pl.akademiaqa.bos.autors.api.payload.UpdateAuthorPayload;
import pl.akademiaqa.bos.autors.api.response.CreateAuthorResponse;
import pl.akademiaqa.bos.autors.api.response.PartialUpdateAuthorResponse;
import pl.akademiaqa.bos.autors.api.response.UpdateAuthorResponse;
import pl.akademiaqa.bos.autors.db.AuthorJpaRepository;
import pl.akademiaqa.bos.autors.domain.Author;
import pl.akademiaqa.bos.autors.service.port.IAuthorService;
import pl.akademiaqa.bos.commons.StringBuilderPlus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static pl.akademiaqa.bos.commons.IsNullOrEmpty.isNullOrEmpty;

@Service
@AllArgsConstructor
public class AuthorService implements IAuthorService {

    private final AuthorJpaRepository repository;

    @Override
    public List<Author> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Author> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Author> findByFirstNameAndLastName(String firstName, String lastName) {
        return repository.findAll()
                .stream()
                .filter(author -> author.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                .filter(author -> author.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Author> findByFirstName(String firstName) {
        return repository.findAll()
                .stream()
                .filter(author -> author.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Author> findByLastName(String lastName) {
        return repository.findAll()
                .stream()
                .filter(author -> author.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public UpdateAuthorResponse updateAuthor(Long id, UpdateAuthorPayload payload) {
        return repository.findById(id)
                .map(author -> {
                    Author updatedAuthor = toUpdatedAuthor(payload, author);
                    // TODO usunąć zapisywanie do bazy
                    // W testach sprawdzić POST -> PUT -> GET
                    repository.save(updatedAuthor);
                    return UpdateAuthorResponse.success(updatedAuthor.getId());
                })
                .orElseGet(() -> UpdateAuthorResponse.failure("Can not find author with id: " + id));
    }

    @Override
    public PartialUpdateAuthorResponse partialUpdateAuthor(Long id, Map<Object, Object> fields) {

        Optional<Author> existingAuthor = repository.findById(id);
        if (existingAuthor.isEmpty()) {
            return PartialUpdateAuthorResponse.failure("Can not find an author with id: " + id);
        }

        AtomicBoolean isError = new AtomicBoolean(false);
        StringBuilderPlus errors = new StringBuilderPlus();

        return existingAuthor
                .map(autor -> {
                    fields.forEach((key, value) -> {
                        Field field = ReflectionUtils.findField(Author.class, (String) key);
                        field.setAccessible(true);
                        if (field.getName().equals("id")) {
                        } else if (field.getName().equals("firstName")) {
                            if (isNullOrEmpty(value)) {
                                isError.set(true);
                                errors.appendLine("firstName must not be blank");
                                return;
                            }
                            if (value.getClass() != String.class) {
                                isError.set(true);
                                errors.appendLine("firstName must be a string");
                                return;
                            }
                            ReflectionUtils.setField(field, autor, value);
                        } else if (field.getName().equals("lastName")) {
                            if (isNullOrEmpty(value)) {
                                isError.set(true);
                                errors.appendLine("lastName must not be blank");
                                return;
                            }
                            if (value.getClass() != String.class) {
                                isError.set(true);
                                errors.appendLine("lastName must be a string");
                                return;
                            }
                            ReflectionUtils.setField(field, autor, value);
                        } else {
                            ReflectionUtils.setField(field, autor, value);
                        }
                    });

                    if (isError.get()) {
                        throw new IllegalArgumentException(errors.toString());
                    } else {
                        repository.save(autor);
                    }

                    return PartialUpdateAuthorResponse.success(autor.getId());

                })
                .orElseGet(() -> PartialUpdateAuthorResponse.failure("Can not find an author with id: " + id));
    }

    @Override
    public ResponseEntity removeById(Long id) {
        Optional<Author> book = repository.findById(id);
        if (book.isPresent()) {
            repository.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }

    private Author toUpdatedAuthor(UpdateAuthorPayload payload, Author author) {
        author.setFirstName(payload.getFirstName());
        author.setLastName(payload.getLastName());
        return author;
    }

    @Override
    public CreateAuthorResponse createAuthor(CreateAuthorPayload payload) {
        Author author = repository.save(toAuthor(payload));
        if (author == null) {
            return CreateAuthorResponse.failure("Can not create an author");
        }
        return CreateAuthorResponse.success(author.getId());
    }

    private Author toAuthor(CreateAuthorPayload payload) {
        return new Author(payload.getFirstName(), payload.getLastName());
    }
}
