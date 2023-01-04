package pl.akademiaqa.bos.books.api.response;

import pl.akademiaqa.bos.commons.Either;

public class UpdateBookResponse extends Either<String, Long> {
    public UpdateBookResponse(boolean success, String left, Long right) {
        super(success, left, right);
    }

    public static UpdateBookResponse success(Long bookId) {
        return new UpdateBookResponse(true, null, bookId);
    }

    public static UpdateBookResponse failure(String error) {
        return new UpdateBookResponse(false, error, null);
    }
}
