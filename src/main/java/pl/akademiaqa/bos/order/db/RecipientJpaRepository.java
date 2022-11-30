package pl.akademiaqa.bos.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akademiaqa.bos.order.domain.Recipient;

import java.util.Optional;

public interface RecipientJpaRepository extends JpaRepository<Recipient, Long> {

    Optional<Recipient> findByEmailIgnoreCase(String email);
}
