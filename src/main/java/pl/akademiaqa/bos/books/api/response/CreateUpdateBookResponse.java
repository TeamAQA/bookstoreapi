package pl.akademiaqa.bos.books.api.response;

import pl.akademiaqa.bos.commons.Either;

public class CreateUpdateBookResponse extends Either<String, Long> {
    public CreateUpdateBookResponse(boolean success, String left, Long right) {
        super(success, left, right);
    }

    public static CreateUpdateBookResponse success(Long bookId) {
        return new CreateUpdateBookResponse(true, null, bookId);
    }

    public static CreateUpdateBookResponse failure(String error) {
        return new CreateUpdateBookResponse(false, error, null);
    }
}
