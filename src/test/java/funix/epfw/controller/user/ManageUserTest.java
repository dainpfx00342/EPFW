package funix.epfw.controller.user;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.user.User;
import funix.epfw.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageUserTest {

    @InjectMocks
    private ManageUser manageUser;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Test
    void testEditUser_NoAuth() {

        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn("redirect:/login");

            String result = manageUser.editUser(model, session);
            assertEquals("redirect:/login", result);
        }
    }

    @Test
    void testEditUser_WithAuth() {

        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn(null);


            List<User> users = Arrays.asList(new User(), new User());
            when(userService.findAllUserOrderByUsername()).thenReturn(users);

            String result = manageUser.editUser(model, session);


            assertEquals(ViewPaths.MANAGE_USER, result);
            verify(model).addAttribute("users", users);
        }
    }
}