package recipes.services;

import org.springframework.stereotype.Service;
import recipes.entities.User;
import recipes.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public boolean save(User user) {
        if (userRepo.findUserByEmail(user.getEmail()) != null) {
            return false;
        }
        userRepo.save(user);
        return true;
    }

    public User findUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    public List<User> findAll() {
        return StreamSupport
                .stream(userRepo.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
