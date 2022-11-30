package pl.akademiaqa.bos.autors.api.response;

import pl.akademiaqa.bos.commons.Either;

public class UpdateAuthorResponse extends Either<String, Long> {
    public UpdateAuthorResponse(boolean success, String left, Long right) {
        super(success, left, right);
    }

    public static UpdateAuthorResponse success(Long bookId) {
        return new UpdateAuthorResponse(true, null, bookId);
    }

    public static UpdateAuthorResponse failure(String error) {
        return new UpdateAuthorResponse(false, error, null);
    }
}
