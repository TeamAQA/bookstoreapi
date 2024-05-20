package pl.akademiaqa.bos;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.akademiaqa.bos.books.service.port.ICatalogInitializerService;
import pl.akademiaqa.bos.user.domain.Role;
import pl.akademiaqa.bos.user.domain.User;
import pl.akademiaqa.bos.user.service.UserService;

@Slf4j
@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final ICatalogInitializerService catalogInitializer;

    @Override
    public void run(String... args) {
        catalogInitializer.initialize();
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role("ROLE_ADMIN"));
            userService.saveUser(new User("Admin", "admin", "MkRBpjDAyGsQox&2xhg8"));
            userService.addRoleToUser("admin", "ROLE_ADMIN");
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
