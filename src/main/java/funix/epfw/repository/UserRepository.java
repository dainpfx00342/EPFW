package funix.epfw.repository;

import funix.epfw.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // Tìm người dùng theo tên đăng nhập
    User findByUsername(String username);

    // Tìm người dùng theo email
    List<User> findAllByOrderByUsername();
}
