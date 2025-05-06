package funix.epfw.controller.auth;

import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.user.User;
import funix.epfw.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class  LoginControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LoginController loginController;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setUsername("validUser");
        validUser.setPassword("validPassword");
        validUser.setRole(Role.BUYER);
    }

    @Test
    void testShowLoginForm() {
        String view = loginController.showLoginForm(session, model);
        assertEquals(ViewPaths.LOGIN, view);
        verify(session).invalidate();  // verify that the session is invalidated
    }

    @Test
    void testLoginSuccess() {
        when(userService.findByUsername(validUser.getUsername())).thenReturn(validUser);

        String view = loginController.login(validUser.getUsername(), validUser.getPassword(), session, model);

        assertEquals("redirect:/home", view);
        verify(userService).findByUsername(validUser.getUsername());
        verify(session).setAttribute("loggedInUser", validUser);
        verify(session).setAttribute("role", validUser.getRole().name());
    }

    @Test
    void testLoginFailure_UserNotFound() {
        String username = "nonExistentUser";
        String password = "password";

        when(userService.findByUsername(username)).thenReturn(null);

        String view = loginController.login(username, password, session, model);

        assertEquals(ViewPaths.LOGIN, view);
        verify(model).addAttribute(Message.ERROR_MESS, "Sai tên đăng nhập hoặc mật khẩu, vui lòng thử lại");
    }

    @Test
    void testLoginFailure_IncorrectPassword() {
        String incorrectPassword = "wrongPassword";

        when(userService.findByUsername(validUser.getUsername())).thenReturn(validUser);

        String view = loginController.login(validUser.getUsername(), incorrectPassword, session, model);

        assertEquals(ViewPaths.LOGIN, view);
        verify(model).addAttribute(Message.ERROR_MESS, "Sai tên đăng nhập hoặc mật khẩu, vui lòng thử lại");
    }

    @Test
    void testLoginAndCreateSession() {
        when(userService.findByUsername(validUser.getUsername())).thenReturn(validUser);

        String view = loginController.login(validUser.getUsername(), validUser.getPassword(), session, model);

        assertEquals("redirect:/home", view);
        verify(userService).findByUsername(validUser.getUsername());
        verify(session).setAttribute("loggedInUser", validUser);
        verify(session).setAttribute("role", validUser.getRole().name());
    }
}