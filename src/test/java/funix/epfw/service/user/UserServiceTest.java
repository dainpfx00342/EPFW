package funix.epfw.service.user;

import funix.epfw.model.user.User;
import funix.epfw.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Khởi tạo @Mock và @InjectMocks
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setUsername("john");

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("alice");

        when(userRepository.findByUsername("alice")).thenReturn(user);

        User found = userService.findByUsername("alice");

        assertNotNull(found);
        assertEquals("alice", found.getUsername());
        verify(userRepository, times(1)).findByUsername("alice");
    }

    @Test
    void testDeleteUserById() {
        userService.deleteUserById(3L);

        verify(userRepository, times(1)).deleteById(3L);
    }

    @Test
    void testFindAllUserOrderByUsername() {
        User u1 = new User(); u1.setUsername("a");
        User u2 = new User(); u2.setUsername("b");

        when(userRepository.findAllByOrderByUsername()).thenReturn(Arrays.asList(u1, u2));

        List<User> users = userService.findAllUserOrderByUsername();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAllByOrderByUsername();
    }

    @Test
    void testFindById_Found() {
        User user = new User();
        user.setId(100L);

        when(userRepository.findById(100L)).thenReturn(Optional.of(user));

        User found = userService.findById(100L);

        assertNotNull(found);
        assertEquals(100L, found.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User found = userService.findById(999L);

        assertNull(found);
    }
}