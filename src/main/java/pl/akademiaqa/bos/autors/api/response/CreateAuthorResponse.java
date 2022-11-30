package pl.akademiaqa.bos.autors.api.response;

import pl.akademiaqa.bos.commons.Either;

public class CreateAuthorResponse extends Either<String, Long> {
    public CreateAuthorResponse(boolean success, String left, Long right) {
        super(success, left, right);
    }

    public static CreateAuthorResponse success(Long bookId) {
        return new CreateAuthorResponse(true, null, bookId);
    }

    public static CreateAuthorResponse failure(String error) {
        return new CreateAuthorResponse(false, error, null);
    }
}
