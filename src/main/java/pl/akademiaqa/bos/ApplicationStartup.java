package pl.akademiaqa.bos;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.akademiaqa.bos.autors.db.AuthorJpaRepository;
import pl.akademiaqa.bos.autors.domain.Author;
import pl.akademiaqa.bos.books.api.payload.CreateUpdateBookPayload;
import pl.akademiaqa.bos.books.service.port.IBookService;
import pl.akademiaqa.bos.books.domain.Book;
import pl.akademiaqa.bos.order.api.payload.CreateOrderItemPayload;
import pl.akademiaqa.bos.order.api.payload.CreateOrderPayload;
import pl.akademiaqa.bos.order.api.payload.CreateRecipientPayload;
import pl.akademiaqa.bos.order.api.response.CreateOrderResponse;
import pl.akademiaqa.bos.order.service.port.IOrderService;


import java.math.BigDecimal;
import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {
    private final IBookService catalog;
    private final IOrderService order;
    private final AuthorJpaRepository authorRepository;

    @Override
    public void run(String... args) {
        initData();
        placeOrder();
    }

    private void initData() {
        Author joshua = new Author("Joshua", "Bloch");
        Author neal = new Author("Neal", "Gafter");
        Author jamesClear = new Author("James", "Clear");

        authorRepository.save(joshua);
        authorRepository.save(neal);
        authorRepository.save(jamesClear);

        CreateUpdateBookPayload effectiveJava = new CreateUpdateBookPayload(
                "Effective Java",
                Set.of(joshua.getId()),
                2005,
                new BigDecimal("79.00"),
                50L);

        CreateUpdateBookPayload javaPuzzlers = new CreateUpdateBookPayload(
                "Java Puzzlers",
                Set.of(joshua.getId(), neal.getId()),
                2018,
                new BigDecimal("99.00"),
                50L);

        CreateUpdateBookPayload atomoweNawyki = new CreateUpdateBookPayload(
                "Atomowe nawyki",
                Set.of(jamesClear.getId()),
                2019,
                new BigDecimal("44.90"),
                50L);

        catalog.createBook(effectiveJava);
        catalog.createBook(javaPuzzlers);
        catalog.createBook(atomoweNawyki);
    }

    private void placeOrder() {
        Book effectiveJava = catalog.findOneByTitle("Effective Java").orElseThrow(() -> new IllegalStateException("Can not find a book"));
        Book puzzlers = catalog.findOneByTitle("Java Puzzlers").orElseThrow(() -> new IllegalStateException("Can not find a book"));

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
                .bookId(effectiveJava.getId())
                .quantity(5)
                .build();

        CreateOrderItemPayload item2 = CreateOrderItemPayload.builder()
                .bookId(puzzlers.getId())
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
        log.info(result.toString());
    }
}
