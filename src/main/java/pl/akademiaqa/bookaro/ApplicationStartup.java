package pl.akademiaqa.bookaro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.akademiaqa.bookaro.catalog.application.port.CatalogUseCase;
import pl.akademiaqa.bookaro.catalog.domain.Book;

import java.util.List;

import static pl.akademiaqa.bookaro.catalog.application.port.CatalogUseCase.*;

@Component
public class ApplicationStartup implements CommandLineRunner {
    private final CatalogUseCase catalog;
    private final String title;

    public ApplicationStartup(CatalogUseCase catalog,
                              @Value("${bookaro.catalog.query}") String title) {
        this.catalog = catalog;
        this.title = title;
    }

    @Override
    public void run(String... args) {
        initData();
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
                    UpdateBookCommand command = UpdateBookCommand
                            .builder()
                            .id(book.getId())
                            .title("Pan Tadeusz Czyli Ostatni Zajazd Na Litwie")
                            .build();

                    UpdateBookResponse response = catalog.updateBook(command);
                    System.out.println("Updating book result: " + response.isSuccess());
                });
    }

    private void initData() {
        catalog.addBook(new CreateBookCommand("Pan Tadeusz", "Adam Mickiewicz", 1834));
        catalog.addBook(new CreateBookCommand("Ogniem i Mieczem", "Henryk Sienkiewicz", 1884));
        catalog.addBook(new CreateBookCommand("Chłopi", "Władysław Reymont ", 1904));
        catalog.addBook(new CreateBookCommand("Pan Wołodyjowski", "Henryk Sienkiewicz ", 1908));
    }
}
