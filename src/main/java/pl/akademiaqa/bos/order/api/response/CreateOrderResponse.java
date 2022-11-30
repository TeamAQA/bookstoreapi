package pl.akademiaqa.bos.order.api.response;

import pl.akademiaqa.bos.commons.Either;

public class CreateOrderResponse extends Either<String, Long> {
    public CreateOrderResponse(boolean success, String left, Long right) {
        super(success, left, right);
    }

    public static CreateOrderResponse success(Long orderId) {
        return new CreateOrderResponse(true, null, orderId);
    }

    public static CreateOrderResponse failure(String error) {
        return new CreateOrderResponse(false, error, null);
    }
}
