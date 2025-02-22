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

    // Add user service
    public void saveUser(User user) { userRepository.save(user);    }
    // Find user by username
    public User findByUsername(String username) {return userRepository.findByUsername(username);}
    // Delete user by id
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    // Find all users order by username
    public List<User> findAllUserOrderByUsername() {
        return userRepository.findAllByOrderByUsername();
    }
    // Find user by id
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
