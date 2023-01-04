package pl.akademiaqa.bos.books.api.response;

import pl.akademiaqa.bos.commons.Either;

public class CreateBookResponse extends Either<String, Long> {
    public CreateBookResponse(boolean success, String left, Long right) {
        super(success, left, right);
    }

    public static CreateBookResponse success(Long bookId) {
        return new CreateBookResponse(true, null, bookId);
    }

    public static CreateBookResponse failure(String error) {
        return new CreateBookResponse(false, error, null);
    }
}
