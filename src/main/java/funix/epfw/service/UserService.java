package funix.epfw.service;

import funix.epfw.model.User;
import funix.epfw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    public List<User> findAllUserOrderByUsername() {
        return userRepository.findAllByOrderByUsername();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
