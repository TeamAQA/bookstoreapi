package pl.akademiaqa.bookstore.books.api.response;

import pl.akademiaqa.bookstore.commons.Either;

public class PartialUpdateBookResponse extends Either<String, Long> {
    public PartialUpdateBookResponse(boolean success, String left, Long right) {
        super(success, left, right);
    }

    public static PartialUpdateBookResponse success(Long bookId) {
        return new PartialUpdateBookResponse(true, null, bookId);
    }

    public static PartialUpdateBookResponse failure(String error) {
        return new PartialUpdateBookResponse(false, error, null);
    }
}
