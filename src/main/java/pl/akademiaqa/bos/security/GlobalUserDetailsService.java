package pl.akademiaqa.bos.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.akademiaqa.bos.user.db.UserEntityRepository;

@AllArgsConstructor
public class GlobalUserDetailsService implements UserDetailsService {

    private final UserEntityRepository repository;
    private final AdminConfig adminConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(adminConfig.getUsername().equalsIgnoreCase(username)){
            return adminConfig.adminUser();
        }
        return repository.findByUsernameIgnoreCase(username)
                .map(UserEntityDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
