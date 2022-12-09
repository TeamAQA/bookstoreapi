package pl.akademiaqa.bos.user.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akademiaqa.bos.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);
}
