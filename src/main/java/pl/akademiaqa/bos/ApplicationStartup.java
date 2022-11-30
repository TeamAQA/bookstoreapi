package pl.akademiaqa.bookstore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.akademiaqa.bookstore.books.api.payload.CreateBookPayload;
import pl.akademiaqa.bookstore.books.api.payload.UpdateBookPayload;
import pl.akademiaqa.bookstore.books.api.response.UpdateBookResponse;
import pl.akademiaqa.bookstore.books.service.port.IBookService;
import pl.akademiaqa.bookstore.books.domain.Book;
import pl.akademiaqa.bookstore.order.api.payload.CreateOrderItemPayload;
import pl.akademiaqa.bookstore.order.api.payload.CreateOrderPayload;
import pl.akademiaqa.bookstore.order.api.payload.CreateRecipientPayload;
import pl.akademiaqa.bookstore.order.api.response.CreateOrderResponse;
import pl.akademiaqa.bookstore.order.service.port.IOrderService;


import java.math.BigDecimal;
import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {
    private final IBookService catalog;
    private final IOrderService order;
    private final String title;

    public ApplicationStartup(IBookService catalog,
                              IOrderService order,
                              @Value("${bookaro.catalog.query}") String title) {
        this.order = order;
        this.catalog = catalog;
        this.title = title;
    }

    @Override
    public void run(String... args) {
        initData();
        searchCatalog();
        placeOrder();
    }

    private void placeOrder() {
        Book panTadeusz = catalog.findOneByTitle("Pan Tadeusz").orElseThrow(() -> new IllegalStateException("Can not find a book"));
        Book chlopi = catalog.findOneByTitle("Chłopi").orElseThrow(() -> new IllegalStateException("Can not find a book"));

        CreateRecipientPayload recipient = CreateRecipientPayload
                .builder()
                .name("Janek Kowalski")
                .phone("745222111")
                .street("Sezamkowa")
                .city("Krakow")
                .zipCode("10-100")
                .email("janekkowalski@o2.pl")
                .build();

        CreateOrderItemPayload item1 = CreateOrderItemPayload.builder()
                .bookId(panTadeusz.getId())
                .quantity(10)
                .build();

        CreateOrderItemPayload item2 = CreateOrderItemPayload.builder()
                .bookId(chlopi.getId())
                .quantity(5)
                .build();

        CreateOrderPayload payload = CreateOrderPayload.builder()
                .item(item1)
                .item(item2)
                .recipient(recipient)
                .build();

        CreateOrderResponse createOrderResponse = order.createOrder(payload);

        Object result = createOrderResponse.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        System.out.println(result);
    }

    private void searchCatalog() {
        findByTitle();
        findAndUpdate();
        findByTitle();
    }

    private void findByTitle() {
        List<Book> books = catalog.findByTitle(title);
        books.forEach(System.out::println);
    }

    private void findAndUpdate() {
        System.out.println("Updating book...");
        catalog.findOneByTitleAndAuthor("Pan Tadeusz", "Adam Mickiewicz")
                .ifPresent(book -> {
                    UpdateBookPayload payload = UpdateBookPayload
                            .builder()
                            .title("Pan Tadeusz Czyli Ostatni Zajazd Na Litwie")
                            .author("Adam Mickiewicz")
                            .year(1834)
                            .price(new BigDecimal("100"))
                            .build();

                    UpdateBookResponse response = catalog.updateBook(book.getId(), payload);

                    String result = response.handle(
                            orderId -> "Created ORDER with id: " + orderId,
                            error -> "Failed to created order: " + error
                    );
                    System.out.println(result);
                });
    }

    private void initData() {
        catalog.createBook(new CreateBookPayload("Pan Tadeusz", "Adam Mickiewicz", 1834, new BigDecimal("100")));
        catalog.createBook(new CreateBookPayload("Ogniem i Mieczem", "Henryk Sienkiewicz", 1884, new BigDecimal("100")));
        catalog.createBook(new CreateBookPayload("Chłopi", "Władysław Reymont ", 1904, new BigDecimal("100")));
        catalog.createBook(new CreateBookPayload("Pan Wołodyjowski", "Henryk Sienkiewicz ", 1908, new BigDecimal("100")));
    }
}
