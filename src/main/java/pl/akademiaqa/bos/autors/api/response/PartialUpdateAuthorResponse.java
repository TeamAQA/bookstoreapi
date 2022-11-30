package pl.akademiaqa.bos.autors.api.response;

import pl.akademiaqa.bos.commons.Either;

public class PartialUpdateAuthorResponse extends Either<String, Long> {
    public PartialUpdateAuthorResponse(boolean success, String left, Long right) {
        super(success, left, right);
    }

    public static PartialUpdateAuthorResponse success(Long bookId) {
        return new PartialUpdateAuthorResponse(true, null, bookId);
    }

    public static PartialUpdateAuthorResponse failure(String error) {
        return new PartialUpdateAuthorResponse(false, error, null);
    }
}
