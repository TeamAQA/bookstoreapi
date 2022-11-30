package pl.akademiaqa.bos.autors.service.port;

import org.springframework.http.ResponseEntity;
import pl.akademiaqa.bos.autors.api.payload.CreateAuthorPayload;
import pl.akademiaqa.bos.autors.api.payload.UpdateAuthorPayload;
import pl.akademiaqa.bos.autors.api.response.CreateAuthorResponse;
import pl.akademiaqa.bos.autors.api.response.PartialUpdateAuthorResponse;
import pl.akademiaqa.bos.autors.api.response.UpdateAuthorResponse;
import pl.akademiaqa.bos.autors.domain.Author;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IAuthorService {

    List<Author> findAll();

    Optional<Author> findById(Long id);

    CreateAuthorResponse createAuthor(CreateAuthorPayload payload);

    List<Author> findByFirstNameAndLastName(String firstName, String lastName);

    List<Author> findByFirstName(String firstName);

    List<Author> findByLastName(String lastName);

    UpdateAuthorResponse updateAuthor(Long id, UpdateAuthorPayload payload);

    PartialUpdateAuthorResponse partialUpdateAuthor(Long id, Map<Object, Object> fields);

    ResponseEntity removeById(Long id);
}
