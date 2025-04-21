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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RegistryControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private HttpSession session;

    @InjectMocks
    private RegistryController registryController;

    private User user;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setRole(Role.BUYER);
    }
    @Test
    void testRegistryGet() {
        String view = registryController.registry(model);
        assertEquals(ViewPaths.REGISTER, view);
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void testRegistryPost_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.findByUsername(user.getUsername())).thenReturn(null);

        String view = registryController.registry(user, bindingResult, model, "password123", session);

        assertEquals("redirect:/home", view);
        verify(userService).saveUser(user);
        verify(session).setAttribute("loggedInUser", user);
    }

    @Test
    void testRegistryPost_ValidationErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = registryController.registry(user, bindingResult, model, "password123", session);

        assertEquals(ViewPaths.REGISTER, view);
        verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
    }

    @Test
    void testRegistryPost_UsernameExists() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.findByUsername(user.getUsername())).thenReturn(new User());

        String view = registryController.registry(user, bindingResult, model, "password123", session);

        assertEquals(ViewPaths.REGISTER, view);
        verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
    }

    @Test
    void testRegistryPost_PasswordMismatch() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.findByUsername(user.getUsername())).thenReturn(null);

        String view = registryController.registry(user, bindingResult, model, "wrongPassword", session);

        assertEquals(ViewPaths.REGISTER, view);
        verify(model).addAttribute(eq("confirmpassError"), anyString());
    }
}
