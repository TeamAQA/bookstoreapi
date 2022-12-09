package pl.akademiaqa.bos.user.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akademiaqa.bos.user.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
