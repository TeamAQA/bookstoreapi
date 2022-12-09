package pl.akademiaqa.bos.user.service;

import pl.akademiaqa.bos.user.domain.Role;
import pl.akademiaqa.bos.user.domain.User;

import java.util.List;

public interface IUserService {
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUser(String username);

    List<User> getUsers();
}
