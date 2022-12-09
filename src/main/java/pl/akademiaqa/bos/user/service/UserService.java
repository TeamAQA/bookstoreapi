package pl.akademiaqa.bos.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.akademiaqa.bos.user.db.RoleRepository;
import pl.akademiaqa.bos.user.db.UserRepository;
import pl.akademiaqa.bos.user.domain.Role;
import pl.akademiaqa.bos.user.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements IUserService, UserDetailsService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsernameIgnoreCase(username);
        if (user.isEmpty()) {
            log.error("User with given username does not exist: " + username);
            throw new UsernameNotFoundException("User with given username does not exist: " + username);
        } else {
            log.error("User found " + username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.get().getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user to the DB: " + user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new user to the DB: " + role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role to the user: " + username + " " + roleName);
        Optional<User> user = userRepo.findByUsernameIgnoreCase(username);
        Role role = roleRepo.findByName(roleName);
        user.get().getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        log.info("Reading user data " + username);
        return userRepo.findByUsernameIgnoreCase(username).get();
    }

    @Override
    public List<User> getUsers() {
        log.info("Reading all users");
        return userRepo.findAll();
    }
}
