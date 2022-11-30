package pl.akademiaqa.bos;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.akademiaqa.bos.books.service.port.ICatalogInitializerService;

@Slf4j
@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final ICatalogInitializerService catalogInitializer;

    @Override
    public void run(String... args) {
        catalogInitializer.initialize();
    }
}
